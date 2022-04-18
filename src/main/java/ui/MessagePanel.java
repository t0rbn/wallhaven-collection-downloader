package ui;

import javax.swing.*;
import java.awt.*;

public class MessagePanel implements UiPanel {

    private static final int ICON_SIZE_PX = 128;

    private final String error;
    private final ImageIcon icon;

    public MessagePanel(MessageType type, String error) {
        this.error = error;
        this.icon = new ImageIcon(new ImageIcon(getClass().getResource(type.iconPath), type.name()).getImage().getScaledInstance(ICON_SIZE_PX, ICON_SIZE_PX, Image.SCALE_SMOOTH));
    }

    @Override
    public JPanel generatePanel() {
        var panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        var label = new JLabel(error, icon, JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        panel.add(label);

        return panel;
    }

    public enum MessageType {
        SUCCESS("/success.png"),
        ERROR("/error.png"),
        PROGRESS("/wallpaper.png");

        private final String iconPath;

        MessageType(String iconPath) {
            this.iconPath = iconPath;
        }
    }
}
