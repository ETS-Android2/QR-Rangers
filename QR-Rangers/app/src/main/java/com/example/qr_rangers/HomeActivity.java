package com.example.qr_rangers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * This is an activity that provides the user with their account information and basic actions.
 * @author Ryan Haskins
 * @version 1.0
 */
public class HomeActivity extends AppCompatActivity{

    private FloatingActionButton scan;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scan = findViewById(R.id.buttonScan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeActivity.this);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        // action bar toggle button setup
        drawerLayout = findViewById(R.id.home_drawer_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.hamburger_open, R.string.hamburger_close);
        // pass the toggle button to the menu
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // make hamburger icon appear
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set on click listeners for all the hamburger menu items
        NavigationView navView = findViewById(R.id.home_nav_view);
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId()==R.id.hamburger_home_button){
                // your code
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_profile_button){
                // your code
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_gallery_button){
                // your code
                Toast.makeText(this, "Gallery Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_map_button){
                // your code
                Toast.makeText(this, "Map Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_setting_button){
                // your code
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.close();
                return true;
            }
            return false;
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
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}