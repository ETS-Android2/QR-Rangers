package com.example.qr_rangers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * This is an activity that provides the user with their account information and basic actions.
 * @author Ryan Haskins, Alexander Salm, Ronan Sandoval
 * @version 1.4
 */
public class HomeActivity extends AppCompatActivity{

    private FloatingActionButton scan;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = loadUser();

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome, " + user.getUsername() + "!");

        TextView totalScanned = findViewById(R.id.codes);
        totalScanned.setText(user.getQRNum() + " Codes Scanned");

        TextView totalScore = findViewById(R.id.highscore);
        totalScore.setText(user.getTotalScore() + " pts.");

        TextView minQR = findViewById(R.id.lowest);
        int min = user.getMinScore();
        if (min == -1) {
            min = 0;
        }
        minQR.setText(min + " pts.");

        TextView maxQR = findViewById(R.id.highest);
        maxQR.setText(user.getMaxScore() + " pts.");

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

        // nearby codes setup
        GpsTracker tracker = new GpsTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
            Log.i("NOTE", "Could not get location...");
        }

        if(tracker.getLocation() != null) {
            Location location = new Location(tracker.getLocation().getLongitude(), tracker.getLocation().getLatitude());
            ArrayList<QRCode> codes = Database.QrCodes.getAll();
            ArrayList<QRCode> userCodes = new ArrayList<>();
            ArrayList<ScannedCode> userScanned = user.getQRList();
            for (ScannedCode code : userScanned) {
                userCodes.add(code.getCode());
            }
            ArrayList<QRCode> filteredCodes = new ArrayList<QRCode>();
            for (int i = 0; i < codes.size() && filteredCodes.size() < 6; i++) {
                if (codes.get(i).getLocation() != null && !userCodes.contains(codes.get(i))) {
                    filteredCodes.add(codes.get(i));
                }
            }
            filteredCodes.sort(new CodeComparatorByDistance(location));
            GridView qrGrid = findViewById(R.id.nearby_codes_grid);
            NearbyCodesAdapter adapter = new NearbyCodesAdapter(this, filteredCodes);
            qrGrid.setAdapter(adapter);

            qrGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i("NOTE", "GRID CLICK");
                    Intent intent = new Intent(HomeActivity.this, QRInfoActivity.class);
                    intent.putExtra("qr", adapter.getItem(i));
                    intent.putExtra("user", user);
                    intent.putExtra("isMyAccount", false);
                    startActivity(intent);
                }
            });
        }

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
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_profile_button){
                // your code
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("isMyAccount", true);
                startActivity(intent);
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_search_button){
                // your code
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_gallery_button){
                // your code
                Intent intent = new Intent(HomeActivity.this, QRListActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("isMyAccount", true);
                startActivity(intent);
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_leaderboard_button){
                Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
                startActivity(intent);
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_map_button){
                // your code
                Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                drawerLayout.close();;
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_admin_button){
                // your code
                Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
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
                    int totalScore = user.getTotalScore();
                    Intent intent = new Intent(HomeActivity.this, ScanResultActivity.class);
                    intent.putExtra("content", intentResult.getContents());
                    intent.putExtra("totalScore",String.valueOf(totalScore));
                    startActivity(intent);
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

    /**
     * Loads user profile information from the database using the locally stored ID
     * @return
     *     The user that has the stored ID
     */
    private User loadUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sharedPreferences.getString("ID", null);
        User localUser = Database.Users.getById(id);
        localUser.setId(id); //added for qr generation support, it needs the id field to be filled out
        return localUser;
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = loadUser();

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome, " + user.getUsername() + "!");

        TextView totalScanned = findViewById(R.id.codes);
        totalScanned.setText(user.getQRNum() + " Codes Scanned");

        TextView totalScore = findViewById(R.id.highscore);
        totalScore.setText(user.getTotalScore() + " pts.");

        TextView minQR = findViewById(R.id.lowest);
        int min = user.getMinScore();
        if (min == -1) {
            min = 0;
        }
        minQR.setText(min + " pts.");

        TextView maxQR = findViewById(R.id.highest);
        maxQR.setText(user.getMaxScore() + " pts.");

        Log.i("USER", "ID: " + user.getId());
        Log.i("USER", Database.Admins.isAdmin(user.getId()) ? "LOGGED IN AS ADMIN" : "LOGGED IN AS BASIC");

        //hide admin button if user is not an admin
        NavigationView navView = findViewById(R.id.home_nav_view);
        navView.getMenu().findItem(R.id.hamburger_admin_button).setVisible(Database.Admins.isAdmin(user.getId()));
    }
}