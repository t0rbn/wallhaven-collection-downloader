package ui;

import wallhaven.WallpaperCollection;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.nio.file.Path;
import java.util.Set;

public class DownloadPanel implements UiPanel {

    private final Set<WallpaperCollection> wallpaperCollections;

    public DownloadPanel(Set<WallpaperCollection> wallpaperCollections) {
        this.wallpaperCollections = wallpaperCollections;
    }

    public JPanel generatePanel() {
        var collectionLabel = new JLabel("Collection");
        var collectionSelector = new JComboBox<>(wallpaperCollections.toArray(new WallpaperCollection[0]));

        var targetLabel = new JLabel("Target Location");
        var targetPathTextField = new JTextField();
        targetPathTextField.setEditable(false);

        var targetSelectButton = new JButton("...");

        var defaultFile = FileSystemView.getFileSystemView().getHomeDirectory();
        var targetFileSelector = new JFileChooser(defaultFile);
        targetFileSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        targetSelectButton.addActionListener(actionEvent -> targetFileSelector.showOpenDialog(null));
        targetFileSelector.addActionListener(actionEvent -> {
            if (targetFileSelector.getSelectedFile() != null) {
                targetPathTextField.setText(targetFileSelector.getSelectedFile().getAbsolutePath());
            }
        });
        targetPathTextField.setText(defaultFile.getAbsolutePath());

        var downloadButton = new JButton("Download");
        downloadButton.setHorizontalAlignment(SwingConstants.RIGHT);
        downloadButton.addActionListener(actionEvent -> {
            Application.download((WallpaperCollection) collectionSelector.getSelectedItem(), Path.of(targetPathTextField.getText()));
        });

        var panel = new JPanel();
        var groupLayout = new GroupLayout(panel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(collectionLabel).addComponent(targetLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(groupLayout.createParallelGroup().addComponent(collectionSelector).addGroup(groupLayout.createSequentialGroup().addComponent(targetPathTextField).addComponent(targetSelectButton)).addComponent(downloadButton, GroupLayout.Alignment.TRAILING))
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(collectionLabel).addComponent(collectionSelector))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(targetLabel).addComponent(targetPathTextField).addComponent(targetSelectButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(downloadButton)
        );

        groupLayout.linkSize(SwingConstants.VERTICAL, targetPathTextField, downloadButton);
        groupLayout.linkSize(SwingConstants.VERTICAL, targetSelectButton, downloadButton);
        groupLayout.linkSize(SwingConstants.VERTICAL, collectionSelector, downloadButton);

        panel.setLayout(groupLayout);
        return panel;
    }
}
