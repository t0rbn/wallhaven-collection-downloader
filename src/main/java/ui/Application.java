package ui;

import wallhaven.WallhavenApiConnector;
import wallhaven.WallpaperCollection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

public class Application {

    private static final JFrame frame;
    private static WallhavenApiConnector apiConnector;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame("Wallhaven Collection Downloader");
        frame.setMinimumSize(new Dimension(400, 100));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        openCredentialsPanel();
    }

    public static void fillFrame(UiPanel panel) {
        frame.setContentPane(panel.generatePanel());
        frame.validate();
        frame.pack();
    }

    public static void openCredentialsPanel() {
        fillFrame(new CredentialsPanel());
    }

    public static void authentify(String username, String apiKey) {
        fillFrame(new MessagePanel(MessagePanel.MessageType.PROGRESS, "fetching collections..."));

        SwingUtilities.invokeLater(() -> {
            Application.apiConnector = new WallhavenApiConnector(username, apiKey);
            Set<WallpaperCollection> collections = null;
            try {
                collections = apiConnector.getUserCollections();
                fillFrame(new DownloadPanel(collections));
            } catch (Exception e) {
                fillFrame(new MessagePanel(MessagePanel.MessageType.ERROR, e.getMessage()));
            }
        });
    }

    public static void download(WallpaperCollection collection, Path destination) {
        fillFrame(new MessagePanel(MessagePanel.MessageType.PROGRESS, "downloading collection " + collection.getName() + "..."));

        SwingUtilities.invokeLater(() -> {
            try {
                apiConnector.downloadCollectionById(collection.getId(), destination);
                fillFrame(new MessagePanel(MessagePanel.MessageType.SUCCESS, "download complete"));
            } catch (URISyntaxException | IOException | InterruptedException e) {
                fillFrame(new MessagePanel(MessagePanel.MessageType.ERROR, e.getMessage()));
            }
        });
    }

}
