/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class ChatApp {
    public static void main(String[] args) {
        // Part 1: Registration
        Login auth = new Login();
        JOptionPane.showMessageDialog(
            null,
            "=== QuickChat Registration ==="
        );
        while (true) {
            String u = JOptionPane.showInputDialog(
                null,
                "Enter username:\nExample: user_"
            );
            if (u == null) System.exit(0);
            String p = JOptionPane.showInputDialog(
                null,
                "Enter password:\nExample: Abcdef1!"
            );
            if (p == null) System.exit(0);
            String c = JOptionPane.showInputDialog(
                null,
                "Enter cell phone (#):\nInclude +27, Example: +271234567890"
            );
            if (c == null) System.exit(0);

            String res = auth.registerUser(u, p, c);
            JOptionPane.showMessageDialog(null, res);
            if ("User successfully captured".equals(res)) {
                break;
            }
        }

        // Part 1: Login
        JOptionPane.showMessageDialog(
            null,
            "=== QuickChat Login ==="
        );
        while (true) {
            String u = JOptionPane.showInputDialog(null, "Username:");
            if (u == null) System.exit(0);
            String p = JOptionPane.showInputDialog(null, "Password:");
            if (p == null) System.exit(0);

            auth.loginUser(u, p);
            String res = auth.returnLoginStatus();
            JOptionPane.showMessageDialog(null, res);
            if (res.startsWith("Welcome ")) {
                break;
            }
        }

        // Part 2: Messaging
        JOptionPane.showMessageDialog(
            null,
            "=== Welcome to QuickChat Messaging ==="
        );
        List<Message> sent = new ArrayList<>();

        while (true) {
            String m = JOptionPane.showInputDialog(
                null,
                "Select:\n"
              + "1) Send Messages\n"
              + "2) Show recently sent\n"
              + "3) Quit"
            );
            if (m == null) System.exit(0);

            int choice;
            try {
                choice = Integer.parseInt(m);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Invalid menu choice. Enter 1, 2 or 3."
                );
                continue;
            }

            switch (choice) {
                case 1:
                    sendMessages(sent);
                    break;
                case 2:
                    showSentMessages(sent);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(
                        null,
                        "Invalid menu choice. Enter 1, 2 or 3."
                    );
            }
        }
    }

    /** Handles the “Send Messages” flow with full validation. */
    private static void sendMessages(List<Message> sent) {
        int n;
        while (true) {
            String ns = JOptionPane.showInputDialog(
                null,
                "How many messages would you like to send?"
            );
            if (ns == null) return;
            try {
                n = Integer.parseInt(ns);
                if (n <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please enter a positive integer."
                );
            }
        }

        Message.resetSequence();
        for (int i = 1; i <= n; i++) {
            Message msgObj;
            // Validate recipient & text together
            while (true) {
                String rec = JOptionPane.showInputDialog(
                    null,
                    String.format(
                        "Message %d/%d – Enter recipient (#):\n+27XXXXXXXXXX",
                        i, n
                    )
                );
                if (rec == null) return;
                String txt = JOptionPane.showInputDialog(
                    null,
                    String.format(
                        "Message %d/%d – Enter text (≤250 chars):",
                        i, n
                    )
                );
                if (txt == null) return;

                try {
                    msgObj = new Message(rec, txt);
                    break;
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage()
                    );
                }
            }

            // Send/Discard/Store prompt
            int opt;
            while (true) {
                String os = JOptionPane.showInputDialog(
                    null,
                    "Choose an option:\n"
                  + "1) Send\n"
                  + "2) Disregard\n"
                  + "3) Store"
                );
                if (os == null) return;
                try {
                    opt = Integer.parseInt(os);
                    if (opt < 1 || opt > 3) throw new NumberFormatException();
                    break;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Invalid choice – enter 1, 2 or 3."
                    );
                }
            }

            // Perform action
            String result = msgObj.sendMessage(opt);
            JOptionPane.showMessageDialog(null, result);

            if (opt == 1) {
                // Show details and count
                JOptionPane.showMessageDialog(null, msgObj.getDetails());
                sent.add(msgObj);
            } else if (opt == 3) {
                try {
                    Message.storeMessage(msgObj);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Error storing message:\n" + ex.getMessage()
                    );
                }
            }
        }

        JOptionPane.showMessageDialog(
            null,
            "Total messages sent: " + sent.size()
        );
    }

    /** Displays all the messages sent so far. */
    private static void showSentMessages(List<Message> sent) {
        if (sent.isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "No messages have been sent yet."
            );
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sent) {
            sb.append(m.getDetails())
              .append("\n-------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}



