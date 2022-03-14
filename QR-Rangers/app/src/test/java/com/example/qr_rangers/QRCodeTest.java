package com.example.qr_rangers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class QRCodeTest {
    private QRCode code = new QRCode("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", null, null);

    @Test
    void scoreTest(){
        assertEquals(code.getScore(), 15);
    }

    @Test
    void equalsTest(){
        QRCode newCode = new QRCode("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", null, null);
        assertTrue(code.equals(newCode));
        QRCode newCode2 = new QRCode("l", null, null);
        assertFalse(code.equals(newCode2));
        assertThrows(IllegalArgumentException.class, () -> {
            code.equals("l");
        });
    }
}
