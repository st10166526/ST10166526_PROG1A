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
     * Constructs a Message with its “flag” of Sent/Stored/Disregard.
     * Throws IllegalArgumentException on any invalid field.
     */
    public Message(String recipient, String text, String flag) {
        if (recipient == null || !checkRecipientCell(recipient)) {
            throw new IllegalArgumentException(
                "Invalid recipient number.\n"
              + "Must start +27 and then 10 digits.\n"
              + "Example: +271234567890"
            );
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(
                "Message text cannot be empty (max 250 chars)."
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
        this.flag      = flag;
        this.hash      = createMessageHash();
    }

    /** Random 10-digit ID. */
    private String generateMessageID() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    /** Validate +27 followed by 10 digits. */
    public boolean checkRecipientCell(String cell) {
        return cell.matches("^\\+27\\d{10}$");
    }

    /** Build an uppercase hash: first2Chars:seq:firstWord+lastWord */
    private String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words  = text.trim().split("\\s+");
        String first    = words[0];
        String last     = words[words.length - 1];
        String raw      = firstTwo + ":" + msgNumber + ":" + (first + last);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes());
            Formatter fmt = new Formatter();
            for (byte b : digest) fmt.format("%02x", b);
            String result = fmt.toString().toUpperCase();
            fmt.close();
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // ——— Getters for use elsewhere ————————————————————————————————
    public String getMessageID()  { return messageID; }
    public String getHash()       { return hash;      }
    public String getRecipient()  { return recipient; }
    public String getText()       { return text;      }
    public String getFlag()       { return flag;      }

    /** Human-readable details */
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

    /** Reset sequence (for repeatable tests) */
    public static void resetSequence() {
        sequence = 0;
    }
}


