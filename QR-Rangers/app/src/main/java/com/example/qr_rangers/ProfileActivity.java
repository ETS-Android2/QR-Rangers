package com.example.qr_rangers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Activity to view profile stats, access gallery, and obtain user code
 * @author Ronan Sandoval
 * @version 1.0
 */
public class ProfileActivity extends AppCompatActivity{

    private Button viewGalleryButton;
    private Button shareProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewGalleryButton = findViewById(R.id.viewgallerybutton);
        viewGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: GO to gallery activity
            }
        });

        shareProfileButton = findViewById(R.id.shareprofilebutton);
        shareProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: GO to Profile QR activity
            }
        });
    }

}