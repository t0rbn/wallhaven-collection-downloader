package wcd;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class Application {

    public static final String DEFAULT_SYNC_DIR = System.getProperty("user.home") + "/Pictures/Wallpapers";

    public static void main(String... args) throws URISyntaxException, IOException, InterruptedException {
        var username = CliUtils.read("Wallhaven Username:");
        var apiKey = CliUtils.readPassword("Wallhaven API Key:");

        CliUtils.write("getting collections...");
        var connector = new WallhavenApiConnector(username, apiKey);
        var collections = connector.getUserCollections();

        CliUtils.write("Found collections '" + collections.stream().map(WallpaperCollection::getName).collect(Collectors.joining("', '")) + "'");

        var collectionNamesToDownload = Arrays.stream(CliUtils.read("Enter collections to download as comma separated list. Leave empty to download all.")
                .split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());


        if (!collectionNamesToDownload.isEmpty()) {
            collections = collections.stream().filter(c -> collectionNamesToDownload.contains(c.getName())).collect(Collectors.toSet());
        }

        var selectedBasePath = CliUtils.read("Target path (default " + DEFAULT_SYNC_DIR +"):");
        var basePath = selectedBasePath.isBlank() ? DEFAULT_SYNC_DIR : selectedBasePath;

        // url -> FS path
        var urlPathMap = new HashMap<String, String>();
        collections.forEach(collection -> {
            collection.getDownloadUrls().forEach(url -> {
                var targetPathFragment = url.split("/");
                var targetPath = basePath + '/' + collection.getName() + '/' + targetPathFragment[targetPathFragment.length - 1];
                urlPathMap.put(url, targetPath);
            });
        });


        // download Wallpapers not in FS
        urlPathMap.keySet().forEach(downloadUrl -> {
            var targetPath = urlPathMap.get(downloadUrl);
            if (!new File(targetPath).exists()) {
                CliUtils.write("creating " + targetPath);
                connector.downloadUrl(downloadUrl, targetPath);
            }
        });

        // delete files not in sync with remote and empty directories\
        Arrays.stream(Objects.requireNonNull(new File(basePath).listFiles()))
                .filter(File::isDirectory)
                        .forEach(collectionDir -> {
                            Arrays.stream(collectionDir.listFiles()).forEach(f -> {
                                if (!urlPathMap.containsValue(f.getPath())) {
                                    CliUtils.write("deleting file " + f);
                                    f.delete();
                                }
                            });
                        });

        Arrays.stream(Objects.requireNonNull(new File(basePath).listFiles())).filter(File::isDirectory).forEach(directory -> {
            if (Objects.requireNonNull(directory.listFiles()).length == 0) {
                CliUtils.write("deleting directory " + directory);
                directory.delete();
            }
        });

        CliUtils.write("sync finished");
    }
}