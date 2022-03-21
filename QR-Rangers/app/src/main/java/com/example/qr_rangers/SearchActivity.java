package com.example.qr_rangers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * An activity that displays options for the user to search for other user profiles
 * through QR code or username
 *
 * @author Jawdat
 * @version 1.1.1
 */
public class SearchActivity extends AppCompatActivity {
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        search_qr = findViewById(R.id.search_qr);

        search_text = findViewById(R.id.search);
        search_text.onActionViewExpanded();
        search_text.setQueryHint("Type Username Here!");
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
                    if (Database.Users.getById(id) != null) {
                        Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user", Database.Users.getById(id));
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
        String query = search_text.getQuery().toString();
        //searched = search.FindUser(query);
        if(!query.trim().isEmpty()) {
            searched = Database.Users.searchSuggestions(query);
        }
        else{
            searched = new ArrayList<User>();
        }

        search_adapter = new CustomList(this, searched);

        Log.i("TAG", query);
        search_list.setAdapter(search_adapter);
        return super.onSearchRequested();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
