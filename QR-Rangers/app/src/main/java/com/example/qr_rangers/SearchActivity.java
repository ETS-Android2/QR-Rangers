package com.example.qr_rangers;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Search search = new Search();
    ListView search_list;
    SearchView search_text;
    ArrayList<User> searched = new ArrayList<>();
    ArrayAdapter<User> search_adapter;
    Button search_qr;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private final float fadeSpeed = (float)1.5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_qr = findViewById(R.id.search_qr);

        search_text = findViewById(R.id.search);
        search_list = findViewById(R.id.search_list);

        search_adapter = new CustomList(this, searched);

        search_list.setAdapter(search_adapter);

        search_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(SearchActivity.this);
                intentIntegrator.setPrompt("Scan a valid user QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                profileIntent.putExtra("user", searched.get(i));
                startActivity(profileIntent);
            }
        });

        search_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                onSearchRequested();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                onSearchRequested();
                return false;
            }
        });

        // action bar toggle button setup
        drawerLayout = findViewById(R.id.search_drawer_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.hamburger_open, R.string.hamburger_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                search_text.setAlpha((float) 1 - (slideOffset * fadeSpeed));
                search_list.setAlpha((float) 1 - (slideOffset * fadeSpeed));
                search_qr.setAlpha((float) 1 - (slideOffset * fadeSpeed));
            }

            public void onDrawerOpened(View drawerView){
                search_text.setClickable(false);
                search_list.setClickable(false);
                search_qr.setClickable(false);
            }

            public void onDrawerClosed(View drawerView){
                search_text.setClickable(true);
                search_list.setClickable(true);
                search_qr.setClickable(true);
            }
        };
        // pass the toggle button to the menu
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // make hamburger icon appear
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navView = findViewById(R.id.search_nav_view);
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId()==R.id.hamburger_home_button){
                // your code
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
                Intent barIntent = new Intent(this, HomeActivity.class);
                startActivity(barIntent);
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_profile_button){
                // your code
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.close();
                return true;
            }
            else if (item.getItemId()==R.id.hamburger_search_button){
                // your code
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                Intent barIntent = new Intent(SearchActivity.this, SearchActivity.class);
                startActivity(barIntent);
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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String id = intentResult.getContents();
                try {
                    if (Database.Users.getById(id, new User("", "", "")) != null) {
                        Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user", Database.Users.getById(id, new User("", "", "")));
                        startActivity(profileIntent);
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

    @Override
    public boolean onSearchRequested() {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            query = intent.getStringExtra(SearchManager.QUERY);
//            searched = search.FindUser(query);
//        }
        String query = search_text.getQuery().toString();
        //searched = search.FindUser(query);
        searched = search.searchSuggestions(query);

        search_adapter = new CustomList(this, searched);

        Log.i("TAG", query);

        search_list.setAdapter(search_adapter);
        return super.onSearchRequested();
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
