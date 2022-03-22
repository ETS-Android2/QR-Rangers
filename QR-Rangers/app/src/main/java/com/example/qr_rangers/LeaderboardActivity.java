package com.example.qr_rangers;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    Search search = new Search();
    ArrayList<User> leaders = new ArrayList<User>();

    ListView board;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }
}
