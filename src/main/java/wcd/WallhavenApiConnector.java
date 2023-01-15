package wcd;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
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
            var id = current.getNumber("id").toString();
            var name = current.getString("label");
            var urls = this.getCollectionDownloadUrls(id);
            collections.add(new WallpaperCollection(name, urls));
        }
        return collections;
    }

    private Set<String> getCollectionDownloadUrls(String id, int page) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder(new URI("https://wallhaven.cc/api/v1/collections/" + userName + "/" + id + "?apikey=" + apiKey + "&page=" + page))
                .GET()
                .build();

        var httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        var responseJson = new JSONObject(response.body());
        var responseCollectionsArray = responseJson.getJSONArray("data");
        var urls = new HashSet<String>();
        for (int i = 0; i < responseCollectionsArray.length(); i++) {
            var current = responseCollectionsArray.getJSONObject(i);
            urls.add(current.getString("path"));
        }

        var currentPage = responseJson.getJSONObject("meta").getInt("current_page");
        var last_page = responseJson.getJSONObject("meta").getInt("last_page");

        if (currentPage < last_page) {
            var returnSet = new HashSet<>(urls);
            returnSet.addAll(this.getCollectionDownloadUrls(id, currentPage + 1));
            return returnSet;
        } else {
            return urls;
        }
    }

    private Set<String> getCollectionDownloadUrls(String id) throws URISyntaxException, IOException, InterruptedException {
        return this.getCollectionDownloadUrls(id, 1);
    }

    public void downloadUrl(String url, String destination) {
        var paramUrl = url + "?apikey=" + apiKey;
        try {
            var destinationPath = Paths.get(destination);
            Files.createDirectories(destinationPath);
            Files.copy(new URL(paramUrl).openStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            Thread.sleep(1500); // circumvent wallhaven api rate limit in the ugliest way possible
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}