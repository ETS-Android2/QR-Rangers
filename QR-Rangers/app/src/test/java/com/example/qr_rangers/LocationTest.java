package com.example.qr_rangers;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocationTest {
    private Location loc = new Location(21.42, 69.4);
    private Location loc2 = new Location();

    @Test
    void TestLongitude() {
        assertEquals(loc.getLongitude(), 21.42, 0.01);
        assertEquals(loc2.getLongitude(), 0, 0.01);
    }

    @Test
    void TestLatitude() {
        assertEquals(loc.getLatitude(), 69.4, 0.01);
        assertEquals(loc2.getLatitude(), 0, 0.01);
    }

    @Test
    void TestLocationEquals() {
        assertTrue(loc.equals(loc));
        assertTrue(loc2.equals(loc2));
        assertFalse(loc.equals(new Location(21.42, 69.4)));
        assertFalse(loc.equals(loc2));
    }
}