/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

public class Login {
    private String storedUsername;
    private String storedPassword;
    private boolean storedLoginStatus;

    /** Username must contain “_” and be ≤5 chars. */
    public boolean checkUserName(String username) {
        return username != null
            && username.contains("_")
            && username.length() <= 5;
    }

    /**
     * Password must be ≥8 chars, contain at least one uppercase,
     * one digit, and one special character.
     */
    public boolean checkPasswordComplexity(String password) {
        return password != null
            && password.matches("^(?=.{8,}$)(?=.*[A-Z])(?=.*\\d)(?=.*\\W).*$");
    }

    /** Cell phone must start with +27 and then 10 digits. */
    public boolean checkCellPhoneNumber(String cell) {
        return cell != null
            && cell.matches("^\\+27\\d{10}$");
    }

    /**
     * Attempts to register; returns an explanatory message.
     */
    public String registerUser(String username,
                               String password,
                               String cell) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted.\n"
                 + "Must contain an underscore and be no more than 5 characters.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted.\n"
                 + "Must be at least 8 characters, contain one uppercase, one digit, one special char.";
        }
        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number is not correctly formatted.\n"
                 + "Must start with +27 and then 10 digits.";
        }
        this.storedUsername = username;
        this.storedPassword = password;
        return "User successfully captured";
    }

    /** Logs in; sets internal status. */
    public void loginUser(String username, String password) {
        this.storedLoginStatus =
               username != null
            && password != null
            && username.equals(storedUsername)
            && password.equals(storedPassword);
    }

    /** Returns explanatory login‐status message. */
    public String returnLoginStatus() {
        if (storedLoginStatus) {
            return "Welcome " + storedUsername + ", nice to see you again";
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    /** Expose the registered user for Part 3 reports */
    public String getStoredUsername() {
        return storedUsername;
    }
}

