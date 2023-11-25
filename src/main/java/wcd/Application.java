package wcd;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String... args) throws URISyntaxException, IOException, InterruptedException{
        if (args.length < 3) {
            System.out.println();
            CliUtils.printHelp();
            return;
        }

        var username = args[0];
        var apiKey = args[1];
        var syncPath = args[2];

        var connector = new WallhavenApiConnector(username, apiKey);
        var allCollections = connector.getUserCollections();
        Set<WallpaperCollection> syncCollections;

        if (args.length == 3) {
            syncCollections = allCollections;
        } else {
            var syncCollectionNames = Arrays.stream(args).skip(3l).collect(Collectors.toSet());
            syncCollections = allCollections.stream().filter(c -> syncCollectionNames.contains(c.getName())).collect(Collectors.toSet());
        }

        // url -> FS path
        var urlPathMap = new HashMap<String, String>();
        syncCollections.forEach(collection -> {
            collection.getDownloadUrls().forEach(url -> {
                var targetPathFragment = url.split("/");
                var targetPath = syncPath + '/' + collection.getName() + '/' + targetPathFragment[targetPathFragment.length - 1];
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
        Arrays.stream(Objects.requireNonNull(new File(syncPath).listFiles()))
                .filter(File::isDirectory)
                        .forEach(collectionDir -> {
                            Arrays.stream(collectionDir.listFiles()).forEach(f -> {
                                if (!urlPathMap.containsValue(f.getPath())) {
                                    CliUtils.write("deleting file " + f);
                                    f.delete();
                                }
                            });
                        });

        Arrays.stream(Objects.requireNonNull(new File(syncPath).listFiles())).filter(File::isDirectory).forEach(directory -> {
            if (Objects.requireNonNull(directory.listFiles()).length == 0) {
                CliUtils.write("deleting directory " + directory);
                directory.delete();
            }
        });

        CliUtils.write("sync finished");
    }
}