/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChatApp {

    public static void main(String[] args) {
        Login auth = new Login();
        // ── Registration ─────────────────────────────────────────────
        JOptionPane.showMessageDialog(null, "=== QuickChat Registration ===");
        while (true) {
            String u = JOptionPane.showInputDialog(null,
                "Enter username (e.g. user_):");
            if (u == null) System.exit(0);
            String p = JOptionPane.showInputDialog(null,
                "Enter password (e.g. Abcdef1!):");
            if (p == null) System.exit(0);
            String c = JOptionPane.showInputDialog(null,
                "Enter cell phone (+27XXXXXXXXXX):");
            if (c == null) System.exit(0);

            String res = auth.registerUser(u, p, c);
            JOptionPane.showMessageDialog(null, res);
            if (res.startsWith("User successfully")) break;
        }

        // ── Login ────────────────────────────────────────────────────
        JOptionPane.showMessageDialog(null, "=== QuickChat Login ===");
        while (true) {
            String u = JOptionPane.showInputDialog(null, "Username:");
            if (u == null) System.exit(0);
            String p = JOptionPane.showInputDialog(null, "Password:");
            if (p == null) System.exit(0);

            auth.loginUser(u, p);
            String res = auth.returnLoginStatus();
            JOptionPane.showMessageDialog(null, res);
            if (res.startsWith("Welcome ")) break;
        }

        // ── Part 3 Manager Setup ────────────────────────────────────
        MessageManager mgr = new MessageManager();
        try {
            mgr.loadFromJson(new File("messages.json"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Could not load messages.json:\n" + ex.getMessage());
        }

        // ── Main Menu ───────────────────────────────────────────────
        mainMenu(auth, mgr);
    }

    private static void mainMenu(Login auth, MessageManager mgr) {
        while (true) {
            String choice = JOptionPane.showInputDialog(null,
                "Select:\n"
              + "1) Send Messages\n"
              + "2) Show Sent Report\n"
              + "3) Other Operations (a–f)\n"
              + "4) Quit");
            if (choice == null) System.exit(0);

            switch (choice) {
                case "1":
                    sendFlow(auth, mgr);
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, mgr.generateReport());
                    break;
                case "3":
                    submenu(auth, mgr);
                    break;
                case "4":
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null,
                        "Invalid choice. Enter 1, 2, 3 or 4.");
            }
        }
    }

    private static void sendFlow(Login auth, MessageManager mgr) {
        Message.resetSequence();

        int n;
        while (true) {
            String ns = JOptionPane.showInputDialog(null,
                "How many messages to send?");
            if (ns == null) return;
            try {
                n = Integer.parseInt(ns);
                if (n <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                    "Please enter a positive integer.");
            }
        }

        for (int i = 1; i <= n; i++) {
            Message msg;
            // Recipient & text
            while (true) {
                String rec = JOptionPane.showInputDialog(null,
                    "Message " + i + "/" + n + " – recipient (+27XXXXXXXXXX):");
                if (rec == null) return;
                String txt = JOptionPane.showInputDialog(null,
                    "Message " + i + "/" + n + " – text (≤250 chars):");
                if (txt == null) return;
                try {
                    msg = new Message(rec, txt, "");
                    break;
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            // Action
            int opt;
            while (true) {
                String os = JOptionPane.showInputDialog(null,
                    "Choose:\n1) Send\n2) Disregard\n3) Store");
                if (os == null) return;
                try {
                    opt = Integer.parseInt(os);
                    if (opt < 1 || opt > 3) throw new NumberFormatException();
                    break;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "Invalid choice. Enter 1, 2 or 3.");
                }
            }

String flag;
switch (opt) {
    case 1:
        flag = "Sent";
        break;
    case 2:
        flag = "Disregard";
        break;
    default:
        flag = "Stored";
        break;
}

            // Rebuild with flag, add to manager
            msg = new Message(msg.getRecipient(), msg.getText(), flag);
            mgr.addMessage(msg);

            JOptionPane.showMessageDialog(null,
                msg.sendMessage(opt));
            if (opt == 1) {
                JOptionPane.showMessageDialog(null, msg.getDetails());
            }
        }
    }

    private static void submenu(Login auth, MessageManager mgr) {
        String s = JOptionPane.showInputDialog(null,
            "a) Sender→Recipient of sent\n"
          + "b) Longest sent message\n"
          + "c) Lookup by Message ID\n"
          + "d) Messages to Recipient\n"
          + "e) Delete by Hash\n"
          + "f) Full Sent Report");
        if (s == null) return;
switch (s.toLowerCase()) {
    case "a":
        List<String> list = mgr.listSentSenderRecipient(
            auth.getStoredUsername());
        JOptionPane.showMessageDialog(null,
            String.join("\n", list));
        break;
    case "b":
        String lm = mgr.longestSentMessage()
            .orElse("No sent messages.");
        JOptionPane.showMessageDialog(null, lm);
        break;
    case "c":
        String id = JOptionPane.showInputDialog(null,
            "Enter Message ID:");
        mgr.findByID(id).ifPresentOrElse(
            m -> JOptionPane.showMessageDialog(null,
                "To: " + m.getRecipient() + "\n" + m.getText()),
            () -> JOptionPane.showMessageDialog(null, "Not found.")
        );
        break;
    case "d":
        String rc = JOptionPane.showInputDialog(null,
            "Enter recipient (+27XXXXXXXXXX):");
        List<Message> hits = mgr.findByRecipient(rc);
        if (hits.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages found.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Message m : hits) {
                sb.append(m.getDetails()).append("\n---\n");
            }
            JOptionPane.showMessageDialog(null, sb);
        }
        break;
    case "e":
        String h = JOptionPane.showInputDialog(null,
            "Enter message hash:");
        boolean d = mgr.deleteByHash(h);
        JOptionPane.showMessageDialog(null,
            d ? "Deleted." : "Not found.");
        break;
    case "f":
        JOptionPane.showMessageDialog(null,
            mgr.generateReport());
        break;
    default:
        JOptionPane.showMessageDialog(null,
            "Invalid option a–f.");
        break;
}
    }}





