/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

public class User {
    private final String username;
    private final String password;
    private final String cell;

    public User(String username, String password, String cell) {
        this.username = username;
        this.password = password;
        this.cell     = cell;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCell()     { return cell; }
}
