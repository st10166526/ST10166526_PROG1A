/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Client
 */
// ChatApp.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatApp extends JFrame {
    private final Login auth;
    private final CardLayout cardLayout;
    private final JPanel cards;

    public ChatApp() {
        super("ChatApp: Registration & Login");
        auth = new Login();
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        initRegistrationPanel();
        initLoginPanel();
        add(cards);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initRegistrationPanel() {
        JPanel regPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JLabel cellLabel = new JLabel("Cell Phone:");
        JTextField cellField = new JTextField(15);
        JLabel msgLabel = new JLabel(" ");
        JButton registerBtn = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 0; regPanel.add(userLabel, gbc);
        gbc.gridx = 1; regPanel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; regPanel.add(passLabel, gbc);
        gbc.gridx = 1; regPanel.add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; regPanel.add(cellLabel, gbc);
        gbc.gridx = 1; regPanel.add(cellField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; regPanel.add(registerBtn, gbc);
        gbc.gridy = 4; regPanel.add(msgLabel, gbc);

        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String cell    = cellField.getText();
            String result  = auth.registerUser(username, password, cell);
            msgLabel.setText(result);
            if ("User successfully captured".equals(result)) {
                cardLayout.show(cards, "loginPanel");
            }
        });

        cards.add(regPanel, "regPanel");
    }

    private void initLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JLabel msgLabel = new JLabel(" ");
        JButton loginBtn = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; loginPanel.add(userLabel, gbc);
        gbc.gridx = 1; loginPanel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; loginPanel.add(passLabel, gbc);
        gbc.gridx = 1; loginPanel.add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; loginPanel.add(loginBtn, gbc);
        gbc.gridy = 3; loginPanel.add(msgLabel, gbc);

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                auth.loginUser(username, password);
                msgLabel.setText(auth.returnLoginStatus());
            }
        });

        cards.add(loginPanel, "loginPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatApp frame = new ChatApp();
            frame.setVisible(true);
        });
    }
}

