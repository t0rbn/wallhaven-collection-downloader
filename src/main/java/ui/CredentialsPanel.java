package ui;

import javax.swing.*;

public class CredentialsPanel implements UiPanel {

    public CredentialsPanel() {
    }

    public JPanel generatePanel() {
        var userNameLabel = new JLabel("Username");
        var apiKeyLabel = new JLabel("API Key");

        var userNameTextField = new JTextField();
        var apiKeyTextfield = new JTextField();

        var loginButton = new JButton("next");
        loginButton.setHorizontalAlignment(SwingConstants.RIGHT);
        loginButton.addActionListener(actionEvent -> {
            var userNameText = userNameTextField.getText();
            var apiKeyText = apiKeyTextfield.getText();

            if (!userNameText.isBlank() && !apiKeyText.isBlank()) {
                Application.authentify(userNameText, apiKeyText);
            }
        });

        var panel = new JPanel();
        var groupLayout = new GroupLayout(panel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(userNameLabel).addComponent(apiKeyLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(groupLayout.createParallelGroup().addComponent(userNameTextField).addComponent(apiKeyTextfield).addComponent(loginButton, GroupLayout.Alignment.TRAILING))
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(userNameLabel).addComponent(userNameTextField))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(apiKeyLabel).addComponent(apiKeyTextfield))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginButton)
        );

        groupLayout.linkSize(SwingConstants.VERTICAL, userNameTextField, loginButton);
        groupLayout.linkSize(SwingConstants.VERTICAL, apiKeyTextfield, loginButton);

        panel.setLayout(groupLayout);
        return panel;
    }

}
