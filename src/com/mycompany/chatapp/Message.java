/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Message {
    private static int sequence = 0;

    private final String messageID;
    private final int msgNumber;
    private final String recipient;
    private final String text;
    private final String hash;

    /**
     * Constructs a Message, validating recipient & text length.
     * Throws IllegalArgumentException on any invalid field.
     */
    public Message(String recipient, String text) {
        if (recipient == null || !checkRecipientCell(recipient)) {
            throw new IllegalArgumentException(
                "Invalid recipient number.\n"
              + "Must start +27 and then 10 digits.\n"
              + "Example: +271234567890"
            );
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(
                "Message text cannot be empty.\n"
              + "Enter up to 250 characters."
            );
        }
        if (text.length() > 250) {
            throw new IllegalArgumentException(
                "Message exceeds 250 chars by "
              + (text.length() - 250)
              + ", please reduce size."
            );
        }

        this.messageID = generateMessageID();
        this.msgNumber = sequence++;
        this.recipient = recipient;
        this.text      = text;
        this.hash      = createMessageHash();
    }

    /** Internal: random 10-digit ID. */
    private String generateMessageID() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    /** Validates a 10-digit numeric ID string. */
    public boolean checkMessageID(String id) {
        return id != null && id.matches("\\d{10}");
    }

    /** Validates cell: +27 followed by 10 digits. */
    public boolean checkRecipientCell(String cell) {
        return cell.matches("^\\+27\\d{10}$");
    }

    /** Builds the UPPERCASE hash: XX:seq:FIRSTLAST */
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = text.trim().split("\\s+");
        String first   = words[0];
        String last    = words[words.length - 1];
        return (firstTwo
              + ":" + msgNumber
              + ":" + (first + last))
              .toUpperCase();
    }

    /**
     * Branches on user choice:
     * 1 = Send, 2 = Disregard, 3 = Store.
     */
    public String sendMessage(int choice) {
        switch (choice) {
            case 1: return "Message successfully sent.";
            case 2: return "Press 0 to delete message.";
            case 3: return "Message successfully stored.";
            default:
                return "Invalid option. Enter 1, 2 or 3.";
        }
    }

    /** Full details to display after sending. */
    public String getDetails() {
        return String.format(
            "MessageID: %s%n"
          + "Hash: %s%n"
          + "Recipient: %s%n"
          + "Text: %s",
          messageID, hash, recipient, text
        );
    }

    /** Appends a JSON line to messages.json (creates if needed). */
    public static void storeMessage(Message m) throws IOException {
        try (FileWriter fw = new FileWriter("messages.json", true)) {
            String safeText = m.text.replace("\"", "\\\"");
            String entry = String.format(
              "{\"id\":\"%s\",\"hash\":\"%s\","
            + "\"recipient\":\"%s\",\"text\":\"%s\"}%n",
              m.messageID, m.hash, m.recipient, safeText
            );
            fw.write(entry);
        }
    }

    // Getters for testing
    public String getMessageID()  { return messageID; }
    public int    getMsgNumber()  { return msgNumber; }
    public String getRecipient()  { return recipient; }
    public String getText()       { return text; }
    public String getHash()       { return hash; }

    /** Resets sequence counter (for repeatable tests). */
    public static void resetSequence() {
        sequence = 0;
    }
}


