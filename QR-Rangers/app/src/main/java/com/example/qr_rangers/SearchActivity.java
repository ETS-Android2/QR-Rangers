package com.example.qr_rangers;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Search search = new Search();
    ListView search_list;
    EditText search_text;
    ArrayList<User> searched = new ArrayList<>();
    ArrayAdapter<User> search_adapter;
    Button search_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_text = findViewById(R.id.search);
        search_button = findViewById(R.id.search_button);
        search_list = findViewById(R.id.search_list);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searched = search.FindUser(query);
        }

        search_adapter = new CustomList(this, searched);

        search_list.setAdapter(search_adapter);

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                profileIntent.putExtra("user", searched.get(i));
                startActivity(profileIntent);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchRequested();
            }
        });
    }

    @Override
    public boolean onSearchRequested() {
        Intent intent = getIntent();
        String query = "3";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            searched = search.FindUser(query);
        }
        query = search_text.getText().toString();
        searched = search.FindUser(query);

        search_adapter = new CustomList(this, searched);

        Log.i("TAG", query);

        search_list.setAdapter(search_adapter);
        return super.onSearchRequested();
    }
}
