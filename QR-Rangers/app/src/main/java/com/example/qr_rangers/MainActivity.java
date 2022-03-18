package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * This is the starting activity that intents to a certain activity depending on
 * if the user has an existing account.
 * @author Ryan Haskins
 * @version 1.1
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String id = loadID();
        if (id != null) {
            if (Database.Users.getById(id) != null) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Error: User not found! Please create a new account.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /**
     * Loads the ID of the player's user account if it exists
     * @return
     *     The string ID of the user, null if the user doesn't exist
     */
    private String loadID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("ID", null);
    }
}