/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.regex.Pattern;

/**
 * Handles all registration/login logic.
 */
// Login.java
public class Login {
    private String storedUsername;
    private String storedPassword;
    private boolean storedLoginStatus;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        return password.matches("^(?=.{8,}$)(?=.*[A-Z])(?=.*\\d)(?=.*\\W).*");
    }

    public boolean checkCellPhoneNumber(String cell) {
        // Supports South African numbers: +27 followed by 10 digits
        // Generated with ChatGPT (OpenAI) on 2025-05-20. See APA style: https://apastyle.apa.org/blog/how-to-cite-chatgpt
        return cell.matches("^\\+27\\d{10}$");
    }

    public String registerUser(String username, String password, String cell) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number is not correctly formatted, please ensure you include your country code";
        }
        this.storedUsername = username;
        this.storedPassword = password;
        return "User successfully captured";
    }

    public boolean loginUser(String username, String password) {
        this.storedLoginStatus = username.equals(this.storedUsername) && password.equals(this.storedPassword);
        return this.storedLoginStatus;
    }

    public String returnLoginStatus() {
        if (this.storedLoginStatus) {
            return "Welcome " + this.storedUsername + ", nice to see you again";
        } else {
            return "Username or password incorrect, please try again";
        }
    }
}

