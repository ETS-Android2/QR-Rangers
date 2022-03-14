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
    void qrList(){
        QRCode code = new QRCode("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", null, null);
        ArrayList<QRCode> list = new ArrayList<QRCode>();
        user.setQRList(list);
        assertEquals(user.getQRList().size(), 0);
        assertEquals(user.getQRNum(), 0);
        assertEquals(user.getScoreMax(), 0);
        assertEquals(user.getScoreMin(), 0);
        assertEquals(user.getScoreSum(), 0);
        list.add(code);
        assertEquals(user.getQRList().size(), 1);
        assertEquals(user.getQRNum(), 1);
        assertEquals(user.getScoreMax(), 15);
        assertEquals(user.getScoreMin(), 15);
        assertEquals(user.getScoreSum(), 15);
        assertTrue(user.getQRList().contains(code));
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
