package com.example.qr_rangers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * This activity introduces new users to the app and gives them the choice to
 * make a new account or continue on an existing one by scanning its generated QR code.
 * @author Ryan Haskins
 * @version 1.3
 */
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        Button loginQR = findViewById(R.id.buttonLogin);
        loginQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(IntroActivity.this);
                intentIntegrator.setPrompt("Scan a valid user QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String id = intentResult.getContents();
                try {
                    if (Database.Users.getById(id) != null) {
                        saveID(id);
                        Toast.makeText(this,
                                "Hello again " + Database.Users.getById(id).getUsername() + "!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Saves the ID of the new user to local storage for future reference
     * @param id
     *     The string ID of the newly made user
     */
    private void saveID(String id) {
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID", id);
        editor.apply();
    }
}