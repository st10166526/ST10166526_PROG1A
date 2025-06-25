package com.mycompany.chatapp;

import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import static org.junit.Assert.*;

public class MessageTest {

    @Before
    public void setup() {
        // Ensure sequence resets before each test
        Message.resetSequence();
    }

    @Test
    public void testLegacyTwoArgConstructorAndFields() {
        // defaults flag = "Sent"
        Message m = new Message("+2712345678", "Hello");
        assertEquals("+2712345678", m.getRecipient());
        assertEquals("Hello",       m.getText());
        assertEquals("Sent",        m.getFlag());
        assertNotNull(m.getMessageID());
        assertEquals(0, m.getMsgNumber());
    }

    @Test
    public void testThreeArgConstructorAndFlag() {
        Message m = new Message("+2712345678", "FooBar", "Stored");
        assertEquals("FooBar",  m.getText());
        assertEquals("Stored",  m.getFlag());
    }

    @Test
    public void testCheckMessageID() {
        Message m = new Message("+2712345678", "Hello");
        String id = m.getMessageID();
        assertTrue(m.checkMessageID(id));
        assertFalse(m.checkMessageID("1234"));
        assertFalse(m.checkMessageID("abcdefghij"));
    }

    @Test
    public void testCheckRecipientCell() {
        Message m = new Message("+2712345678", "Hello");
        assertTrue(m.checkRecipientCell("+2712345678"));
        assertFalse(m.checkRecipientCell("0712345678"));     // missing +27
        assertFalse(m.checkRecipientCell("+270712345678"));  // too many digits
    }

    @Test
    public void testSequenceIncrement() {
        Message.resetSequence();
        Message a = new Message("+2712345678", "A");
        Message b = new Message("+2712345678", "B");
        Message c = new Message("+2712345678", "C");

        assertEquals(0, a.getMsgNumber());
        assertEquals(1, b.getMsgNumber());
        assertEquals(2, c.getMsgNumber());
    }

    @Test
    public void testSendMessage() {
        Message m = new Message("+2712345678", "Test");
        assertEquals("Message successfully sent.",      m.sendMessage(1));
        assertEquals("Press 0 to delete message.",     m.sendMessage(2));
        assertEquals("Message successfully stored.",   m.sendMessage(3));
        assertEquals("Invalid option. Enter 1, 2 or 3.", m.sendMessage(42));
    }

    @Test
    public void testCreateMessageHash() {
        Message.resetSequence();
        Message m = new Message("+2712345678", "Hello World");
        String id = m.getMessageID();

        // Rebuild the raw input exactly as in createMessageHash():
        String raw = (id.substring(0,2) + ":0:HELLOWORLD").toUpperCase();
        String expected = sha256Hex(raw);

        assertEquals(expected, m.getHash());
    }

    // helper to mirror the SHA-256 logic
    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            Formatter fmt = new Formatter();
            for (byte b : digest) fmt.format("%02x", b);
            String hex = fmt.toString().toUpperCase();
            fmt.close();
            return hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}




