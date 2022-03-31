package com.example.qr_rangers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UserTest {
    private User user = new User("TestUser", "testuser@gmail.com", "7802374823");

    @Test
    void userName(){
        assertEquals(user.getUsername(), "TestUser");
    }

    @Test
    void phoneNumber(){
        assertEquals(user.getPhoneNumber(), "7802374823");
    }

    @Test
    void email(){
        assertEquals(user.getEmail(), "testuser@gmail.com");
    }

    @Test
    void equalsTest(){
        User newUser = new User("TestUser", "a@a", "1");
        assertTrue(user.equals(newUser));
        User newUser2 = new User("TestUser2", "a@a", "1");
        assertFalse(user.equals(newUser2));
        assertThrows(IllegalArgumentException.class, () -> {
            user.equals("l");
        });
    }
}
