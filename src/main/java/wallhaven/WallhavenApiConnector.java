package wallhaven;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;


public class WallhavenApiConnector {

    private final String userName;
    private final String apiKey;

    public WallhavenApiConnector(String userName, String apiKey) {
        this.userName = userName;
        this.apiKey = apiKey;
    }

    public Set<WallpaperCollection> getUserCollections() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("getting collections");
        var request = HttpRequest.newBuilder(new URI("https://wallhaven.cc/api/v1/collections/" + userName + "?apikey=" + apiKey))
                .GET()
                .build();

        var httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("failed to login - " + response.body());
        }

        var responseCollectionsArray = new JSONObject(response.body()).optJSONArray("data");
        if (responseCollectionsArray == null) {
            throw new IllegalStateException("error parsing collections from api; response: " + response.body());
        }

        var collections = new HashSet<WallpaperCollection>();
        for (int i = 0; i < responseCollectionsArray.length(); i++) {
            var current = responseCollectionsArray.getJSONObject(i);
            collections.add(new WallpaperCollection(current.getNumber("id").toString(), current.getString("label")));
        }
        return collections;
    }

    public void downloadCollectionById(String id, Path destination) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder(new URI("https://wallhaven.cc/api/v1/collections/" + userName + "/" + id + "?apikey=" + apiKey))
                .GET()
                .build();

        var httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        var responseCollectionsArray = new JSONObject(response.body()).getJSONArray("data");
        var urls = new HashSet<String>();
        for (int i = 0; i < responseCollectionsArray.length(); i++) {
            var current = responseCollectionsArray.getJSONObject(i);
            urls.add(current.getString("path"));
        }

        urls.forEach((url) -> {
            var filename = url.split("/")[url.split("/").length - 1];
            try {
                Files.copy(new URL(url).openStream(), Paths.get(destination.toString(), '/' + filename), StandardCopyOption.REPLACE_EXISTING);
                Thread.sleep(1500); // circumvent wallhaven api rate limit in the ugliest way possible
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
