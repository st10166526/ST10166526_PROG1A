/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    private Login auth;

    @Before
    public void setUp() {
        auth = new Login();
    }

    @Test
    public void testCheckUserName() {
        // valid: contains underscore, ≤ 5 chars
        assertTrue(auth.checkUserName("user_"));
        // invalid: missing underscore
        assertFalse(auth.checkUserName("userName"));
    }

    @Test
    public void testCheckPasswordComplexity() {
        // valid: ≥8 chars + uppercase + digit + special
        assertTrue(auth.checkPasswordComplexity("Abcd3f!g"));
        // invalid: no uppercase/digit/special
        assertFalse(auth.checkPasswordComplexity("abcdefg"));
    }

    @Test
    public void testCheckCellPhoneNumber() {
        // valid: +27 + 10 digits
        assertTrue(auth.checkCellPhoneNumber("+271234567890"));
        // invalid: missing +27
        assertFalse(auth.checkCellPhoneNumber("0712345678"));
    }

    @Test
    public void testRegisterSuccess() {
        String msg = auth.registerUser("A_id", "Abcd3f!g", "+271234567890");
        assertEquals("User successfully captured", msg);
    }

    @Test
    public void testReturnLoginStatusTrue() {
        // register and then log in with correct credentials
        auth.registerUser("B_id", "Abcdef1!", "+271234567890");
        auth.loginUser("B_id", "Abcdef1!");
        assertEquals(
            "Welcome B_id, nice to see you again",
            auth.returnLoginStatus()
        );
    }

    @Test
    public void testReturnLoginStatusFalse() {
        // register with one password, attempt login with another
        auth.registerUser("C_id", "Abcdef1!", "+271234567890");
        auth.loginUser("C_id", "WrongPass1!");
        assertEquals(
            "Username or password incorrect, please try again",
            auth.returnLoginStatus()
        );
    }
}




