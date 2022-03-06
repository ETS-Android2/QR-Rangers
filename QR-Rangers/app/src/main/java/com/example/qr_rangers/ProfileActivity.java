package com.example.qr_rangers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 * Activity to view profile stats, access gallery, and obtain user code
 * @author Ronan Sandoval
 * @version 1.0
 */
public class ProfileActivity extends AppCompatActivity{

    private TextView totalScoreText;
    private TextView codesScannedText;
    private TextView scoreRankingText;
    private TextView scanRankingText;

    private Button viewGalleryButton;
    private Button shareProfileButton;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private User user;

    private final float fadeSpeed = (float)1.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra("user");

        totalScoreText = findViewById(R.id.totalscore);
        String totalScore = String.valueOf(user.getScoreSum()) + " pts.";
        totalScoreText.setText(totalScore);

        codesScannedText = findViewById(R.id.codesscanned);
        String codesScanned = String.valueOf(user.getQRNum());
        codesScannedText.setText(String.valueOf(codesScanned));

        scoreRankingText = findViewById(R.id.scoreranking);

        scanRankingText = findViewById(R.id.scanranking);

        viewGalleryButton = findViewById(R.id.viewgallerybutton);
        viewGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "View gallery clicked", Toast.LENGTH_SHORT).show();
                //TODO: GO to gallery activity
            }
        });

        shareProfileButton = findViewById(R.id.shareprofilebutton);
        shareProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap code = new QRGenerator(new User("alikayqrtest")).getQrCode();
                new ViewQrCodeFragment(code).show(getSupportFragmentManager(), "Edit_Session");
            }
        });


        // action bar toggle button setup
        drawerLayout = findViewById(R.id.profile_drawer_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.hamburger_open, R.string.hamburger_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                viewGalleryButton.setAlpha((float) 1 - (slideOffset * fadeSpeed));
                shareProfileButton.setAlpha((float)1 - (slideOffset * fadeSpeed));
            }

            public void onDrawerOpened(View drawerView){
                viewGalleryButton.setClickable(false);
                shareProfileButton.setClickable(false);
            }

            public void onDrawerClosed(View drawerView){
                viewGalleryButton.setClickable(true);
                shareProfileButton.setClickable(true);
            }
        };
        // pass the toggle button to the menu
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // make hamburger icon appear
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set on click listeners for all the hamburger menu items
        NavigationView navView = findViewById(R.id.profile_nav_view);
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId()==R.id.hamburger_home_button){
                // your code
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
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