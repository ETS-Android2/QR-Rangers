package com.example.qr_rangers;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import org.junit.jupiter.api.Test;

public class SearchTest {
    @Test
    void searchTest(){
        Search search = new Search();
        User user = search.FindUser("Shashank9");
        Log.i("", "HI");
        Log.i("", user.getUsername());
        assertEquals(user.getUsername(), "Shashank9");
    }
}
