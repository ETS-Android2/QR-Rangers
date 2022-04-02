package com.example.qr_rangers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class QRCodeTest {
    String codeInfo = "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6";
    private QRCode codeWithoutInfoHiding = new QRCode(codeInfo, null);
    private QRCode codeWithInfoHiding = new QRCode(codeInfo,null,true);

    @Test
    void infoTest(){assertEquals(codeWithoutInfoHiding.getCodeInfo(),codeInfo);}

    @Test
    void infoHiddenTest(){assertFalse(codeInfo == codeWithInfoHiding.getCodeInfo());}

    @Test
    void scoreTestWithoutHiding(){
        assertEquals(codeWithoutInfoHiding.getScore(), 15);
    }

    @Test
    void scoreTestWithHiding(){assertEquals(codeWithoutInfoHiding.getScore(),15);}

    @Test
    void equalsTest(){
        QRCode newCode = new QRCode(codeInfo, null);
        assertTrue(codeWithoutInfoHiding.equals(newCode));

        QRCode newCode2 = new QRCode("l", null);
        assertFalse(codeWithoutInfoHiding.equals(newCode2));

        assertThrows(IllegalArgumentException.class, () -> {
            codeWithoutInfoHiding.equals("l");
        });
    }
    @Test
    void equalsWithHiddenTest(){
        QRCode newHideInfoCode = new QRCode(codeInfo, null,true);
        assertTrue(codeWithInfoHiding.equals(newHideInfoCode));

        QRCode newHideInfoCode2 = new QRCode("l", null,true);
        assertFalse(codeWithInfoHiding.equals(newHideInfoCode2));
        assertThrows(IllegalArgumentException.class, () -> {
            codeWithInfoHiding.equals("l");
        });
    }
}
