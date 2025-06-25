package com.mycompany.chatapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

public class Message {
    private static int sequence = 0;

    private final String messageID;
    private final int    msgNumber;
    private final String recipient;
    private final String text;
    private final String flag;
    private final String hash;

    /** 
     * Legacy two‐arg constructor so old tests (new Message(recipient, text)) still compile.
     * Defaults the “flag” to “Sent”.
     */
    public Message(String recipient, String text) {
        this(recipient, text, "Sent");
    }

    /**
     * Full constructor: you must supply “Sent”, “Disregard” or “Stored”.
     * Validates recipient and text.
     */
    public Message(String recipient, String text, String flag) {
        if (recipient == null || !checkRecipientCell(recipient)) {
            throw new IllegalArgumentException(
                "Invalid recipient number.\n" +
                "Must start +27 and then 8 digits.\n" +
                "Example: +271234567890"
            );
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(
                "Message text cannot be empty (max 250 chars)."
            );
        }
        if (text.length() > 250) {
            throw new IllegalArgumentException(
                "Message exceeds 250 chars by " +
                (text.length() - 250) +
                ", please reduce size."
            );
        }

        this.messageID = generateMessageID();
        this.msgNumber = sequence++;
        this.recipient = recipient;
        this.text      = text;
        this.flag      = flag;
        this.hash      = createMessageHash();
    }

    /** Random 10‐digit numeric ID. */
    private String generateMessageID() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    /** Checks a 10‐digit numeric string. */
    public boolean checkMessageID(String id) {
        return id != null && id.matches("\\d{10}");
    }

    /** Validate +27 followed by exactly 10 digits. */
    public boolean checkRecipientCell(String cell) {
        return cell != null && cell.matches("^\\+27\\d{8}$");
    }

    /** Builds a SHA‐256 hash of firstTwoChars:seq:firstWord+lastWord (uppercased). */
    private String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words  = text.trim().split("\\s+");
        String first    = words[0];
        String last     = words[words.length - 1];
        String raw      = (firstTwo + ":" + msgNumber + ":" + (first + last)).toUpperCase();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes());
            Formatter fmt = new Formatter();
            for (byte b : digest) fmt.format("%02x", b);
            String hex = fmt.toString().toUpperCase();
            fmt.close();
            return hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // ——— Public getters for tests and display ——————————————————

    /** Unique 10‐digit message ID. */
    public String getMessageID()  { return messageID; }
    /** Zero‐based sequence number. */
    public int    getMsgNumber()  { return msgNumber;  }
    /** The +27… recipient. */
    public String getRecipient()  { return recipient;  }
    /** The message body. */
    public String getText()       { return text;       }
    /** “Sent”, “Disregard” or “Stored”. */
    public String getFlag()       { return flag;       }
    /** SHA‐256 hash string. */
    public String getHash()       { return hash;       }

    /** Human‐readable full detail. */
    public String getDetails() {
        return String.format(
            "MessageID: %s%nHash: %s%nFlag: %s%nRecipient: %s%nText: %s",
            messageID, hash, flag, recipient, text
        );
    }

    /**
     * Branches on user choice:
     * 1 = Send, 2 = Disregard, 3 = Store.
     */
    public String sendMessage(int choice) {
        return switch (choice) {
            case 1 -> "Message successfully sent.";
            case 2 -> "Press 0 to delete message.";
            case 3 -> "Message successfully stored.";
            default -> "Invalid option. Enter 1, 2 or 3.";
        };
    }

    /** Reset sequence counter (for repeatable tests). */
    public static void resetSequence() {
        sequence = 0;
    }
}



