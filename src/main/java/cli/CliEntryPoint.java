package cli;

import wallhaven.WallhavenApiConnector;

import java.io.IOException;
import java.net.URISyntaxException;

public class CliEntryPoint {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        var user = UserInputPrompt.prompt("wallhaven user name:");
        var apiKey = UserInputPrompt.prompt("wallhaven api key:");

        var wallhavenConnector = new WallhavenApiConnector(user, apiKey);
        var userCollections = wallhavenConnector.getUserCollections();

        System.out.println("Your Wallhaven collections: ");
        for (int i = 0; i < userCollections.size(); i++) {
            System.out.println(i + ": " + userCollections.get(i).getName());
        }

        var selectedIndex = Integer.parseInt(UserInputPrompt.prompt("Index to download: "));
        var destPath = UserInputPrompt.prompt("Output dir");
        wallhavenConnector.downloadCollectionById(userCollections.get(selectedIndex).getId(), destPath);
    }

}
