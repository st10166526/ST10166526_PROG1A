/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.nio.file.Files;
import static org.junit.Assert.*;

public class MessageTest {
    @Before
    public void setup() {
        Message.resetSequence();
        File f = new File("messages.json");
        if(f.exists()) f.delete();
    }

    @Test
    public void testCheckMessageID() {
        assertTrue(new Message("+2712345678","Hi").checkMessageID("0123456789"));
        assertFalse(new Message("+2712345678","Hi").checkMessageID("ABC123"));
    }

    @Test
    public void testCheckRecipientCell() {
        assertTrue(new Message("+2712345678","Hi").checkRecipientCell("+2712345678"));
        assertFalse(new Message("+2712345678","Hi").checkRecipientCell("0712345678"));
    }

    @Test
    public void testCreateMessageHash() {
        Message.resetSequence();
        Message m = new Message("+2712345678","Hello World");
        String id = m.getMessageID();
        String expected = id.substring(0,2) + ":0:HELLOWORLD";
        assertEquals(expected, m.getHash());
    }

    @Test
    public void testSendMessage() {
        Message m = new Message("+2712345678","Test");
        assertEquals("Message successfully sent.",   m.sendMessage(1));
        assertEquals("Press 0 to delete message.",   m.sendMessage(2));
        assertEquals("Message successfully stored.", m.sendMessage(3));
    }

    @Test
    public void testSequenceIncrement() {
        Message m1 = new Message("+2712345678","A");
        Message m2 = new Message("+2712345678","B");
        assertEquals(0, m1.getMsgNumber());
        assertEquals(1, m2.getMsgNumber());
    }

    @Test
    public void testStoreCreatesJSON() throws Exception {
        Message m = new Message("+2712345678","JSON");
        Message.storeMessage(m);
        File f = new File("messages.json");
        assertTrue(f.exists());
        String content = Files.readString(f.toPath());
        assertTrue(content.contains(m.getMessageID()));
    }
}
