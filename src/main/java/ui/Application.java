package ui;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Application {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        var credentialsPanel = new CredentialsPanel();

        var frame = new JFrame();
        frame.setSize(400, 300);
        frame.add(credentialsPanel.generatePanel());
        frame.setVisible(true);

//
//        var user = UserInputPrompt.prompt("wallhaven user name:");
//        var apiKey = UserInputPrompt.prompt("wallhaven api key:");
//
//        var wallhavenConnector = new WallhavenApiConnector(user, apiKey);
//        var userCollections = wallhavenConnector.getUserCollections();
//
//        System.out.println("Your Wallhaven collections: ");
//        for (int i = 0; i < userCollections.size(); i++) {
//            System.out.println(i + ": " + userCollections.get(i).getName());
//        }
//
//        var selectedIndex = Integer.parseInt(UserInputPrompt.prompt("Index to download: "));
//        var destPath = UserInputPrompt.prompt("Output dir");
//        wallhavenConnector.downloadCollectionById(userCollections.get(selectedIndex).getId(), destPath);
    }

}
