package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is an activity that provides the means for the user to create a new account.
 * @author Ryan Haskins
 * @version 1.1
 */
public class CreateAccountActivity extends AppCompatActivity {

    private Button create;
    private EditText username;
    private TextView warning;
    User newUser;

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
                    newUser = new User(username.getText().toString(), "", "");
                    User dbUser = Database.Users.add(newUser);
                    saveID(dbUser.getId());
                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(CreateAccountActivity.this, "Hello " + dbUser.getUsername(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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