package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is an activity that provides the means for the user to create a new account.
 * @author Ryan Haskins
 * @version 1.0
 */
public class CreateAccountActivity extends AppCompatActivity {

    private Button create;
    private EditText username;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        create = findViewById(R.id.buttonCreate);
        username = findViewById(R.id.editUsername);
        warning = findViewById(R.id.nameWarning);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warning.setVisibility(View.GONE);
                if (username.getText().toString().trim().isEmpty()) {
                    warning.setText("Please enter a username");
                    warning.setVisibility(View.VISIBLE);
                }
                else {
                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(CreateAccountActivity.this, "Hello " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}