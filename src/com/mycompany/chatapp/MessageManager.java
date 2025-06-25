/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Holds and categorises all messages into arrays:
 *   - sent
 *   - stored
 *   - disregarded
 *   - messageHashes
 *   - messageIDs
 *
 * Implements Part 3 operations (a)–(f).
 */
public class MessageManager {
    private final List<Message> sent        = new ArrayList<>();
    private final List<Message> stored      = new ArrayList<>();
    private final List<Message> disregarded = new ArrayList<>();

    public final List<String> messageHashes = new ArrayList<>();
    public final List<String> messageIDs    = new ArrayList<>();

    /** Load each JSON line in messages.json into the appropriate array */
    public void loadFromJson(File jsonFile) throws IOException {
        String all = Files.readString(jsonFile.toPath());
        ObjectMapper mapper = new ObjectMapper();
        for (String line : all.split("\\R")) {
            if (line.isBlank()) continue;
            Map<String,String> map = mapper.readValue(
                line, new TypeReference<>() {});
            Message msg = new Message(
                map.get("recipient"),
                map.get("text"),
                map.get("flag")
            );
            categorize(msg);
        }
    }

    /** Add a newly created message */
    public void addMessage(Message msg) {
        categorize(msg);
    }

    private void categorize(Message msg) {
        switch (msg.getFlag().toLowerCase()) {
            case "sent":       sent.add(msg);        break;
            case "stored":     stored.add(msg);      break;
            case "disregard":  disregarded.add(msg); break;
            default: throw new IllegalArgumentException("Unknown flag: " + msg.getFlag());
        }
        messageHashes.add(msg.getHash());
        messageIDs.add(msg.getMessageID());
    }

    /** a) “Display the sender and recipient of all sent messages.” */
    public List<String> listSentSenderRecipient(String senderName) {
        return sent.stream()
            .map(m -> String.format("From %s → To %s", senderName, m.getRecipient()))
            .collect(Collectors.toList());
    }

    /** b) “Display the longest sent message.” */
    public Optional<String> longestSentMessage() {
        return sent.stream()
            .map(Message::getText)
            .max(Comparator.comparingInt(String::length));
    }

    /** c) “Search for a message ID and display recipient+message.” */
    public Optional<Message> findByID(String id) {
        return sent.stream()
            .filter(m -> m.getMessageID().equals(id))
            .findFirst();
    }

    /** d) “Search for all the messages sent to a particular recipient.” */
    public List<Message> findByRecipient(String recipient) {
        return sent.stream()
            .filter(m -> m.getRecipient().equals(recipient))
            .collect(Collectors.toList());
    }

    /** e) “Delete a message using the message hash.” */
    public boolean deleteByHash(String hash) {
        Optional<Message> o = sent.stream()
            .filter(m -> m.getHash().equals(hash))
            .findFirst();
        if (o.isPresent()) {
            sent.remove(o.get());
            return true;
        }
        return false;
    }

    /** f) “Display a report that lists all sent messages in detail.” */
    public String generateReport() {
        StringBuilder sb = new StringBuilder("=== Sent Messages Report ===\n");
        for (Message m : sent) {
            sb.append("ID:    ").append(m.getMessageID()).append("\n")
              .append("Hash:  ").append(m.getHash()).append("\n")
              .append("To:    ").append(m.getRecipient()).append("\n")
              .append("Text:  ").append(m.getText()).append("\n")
              .append("------------------------------\n");
        }
        return sb.toString();
    }
}
