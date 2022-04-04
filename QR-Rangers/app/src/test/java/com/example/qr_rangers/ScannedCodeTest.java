package com.example.qr_rangers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ScannedCodeTest {
    String codeInfo = "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6";

    @Test
    void userTest() {
        User user = new User("test", "test", "test");
        ScannedCode scannedCode = new ScannedCode(new QRCode(codeInfo, null), user, null, null, null);
        assertEquals(user, scannedCode.getUser());

        User user2 = new User("test2", "test2", "test2");
        scannedCode.setUser(user2);
        assertEquals(user2, scannedCode.getUser());
    }

    @Test
    void locationTest() {
        User user = new User("test", "test", "test");
        Location location = new Location(10, 10);
        ScannedCode scannedCode = new ScannedCode(new QRCode(codeInfo, null), user, location, null, null);
        assertEquals(location, scannedCode.getLocationScanned());

        Location location2 = new Location(20, 20);
        scannedCode.setLocationScanned(location2);
        assertEquals(location2, scannedCode.getLocationScanned());
    }

    @Test
    void commentTest() {
        User user = new User("test", "test", "test");
        String comment = "test comment";
        ScannedCode scannedCode = new ScannedCode(new QRCode(codeInfo, null), user, null, comment, null);
        assertEquals(comment, scannedCode.getComment());

        String comment2 = "tested comment";
        scannedCode.setComment(comment2);
        assertEquals(comment2, scannedCode.getComment());
    }

    @Test
    void pictureTest() {
        User user = new User("test", "test", "test");
        String picture = "test picture";
        ScannedCode scannedCode = new ScannedCode(new QRCode(codeInfo, null), user, null, null, picture);
        assertEquals(picture, scannedCode.getPhoto());

        String picture2 = "test picture 2";
        scannedCode.setPhoto(picture2);
        assertEquals(picture2, scannedCode.getPhoto());
    }

    @Test
    void codeTest() {
        User user = new User("test", "test", "test");
        QRCode code = new QRCode(codeInfo, new Location(10, 10));
        ScannedCode scannedCode = new ScannedCode(code, user, null, null, null);
        assertEquals(code, scannedCode.getCode());
        assertEquals(codeInfo, scannedCode.getCode().getCodeInfo());

        QRCode code2 = new QRCode(codeInfo, new Location(2, 2));
        scannedCode.setCode(code2);
        assertEquals(code2, scannedCode.getCode());

    }

}
