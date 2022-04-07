package ui;

import javax.swing.*;

public class CredentialsPanel {

    public CredentialsPanel() {
    }

    public JPanel generatePanel() {
        var userNameLabel = new JLabel("Username");
        var apiKeyLabel = new JLabel("API KEy");

        var userNameTextField = new JTextField();
        var apiKeyTextfield = new JTextField();


        var panel = new JPanel();

        var groupLayout = new GroupLayout(panel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(userNameLabel).addComponent(apiKeyLabel))
                .addGroup(groupLayout.createParallelGroup().addComponent(userNameTextField).addComponent(apiKeyTextfield)
                ));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(userNameLabel).addComponent(userNameTextField))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(apiKeyLabel).addComponent(apiKeyTextfield)
                ));

        panel.setLayout(groupLayout);
        return panel;
    }
}
