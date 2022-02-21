package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This is an activity that provides the user with their account information and basic actions.
 * @author Ryan Haskins
 * @version 1.0
 */
public class HomeActivity extends AppCompatActivity {

    private Button scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scan = findViewById(R.id.buttonScan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Oh Yeah, It's Scanning Time! :sunglasses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}