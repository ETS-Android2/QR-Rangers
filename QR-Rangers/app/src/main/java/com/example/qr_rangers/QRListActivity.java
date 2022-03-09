package com.example.qr_rangers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class QRListActivity extends AppCompatActivity {
    TextView nameText;

    GridView qrGrid;
    QRListAdapter qrListAdapter;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_list);

        user = (User) getIntent().getSerializableExtra("user");

        nameText = findViewById(R.id.qr_list_name);
        String nameString = user.getUsername() + "'s Gallery";
        nameText.setText(nameString);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ArrayList<QRCode> qrList = user.getQRList();
        qrList.add(new QRCode("test", null, null));
        qrList.add(new QRCode("test", null, null));
        qrList.add(new QRCode("test1", null, null));
        qrList.add(new QRCode("test", null, null));
        qrList.add(new QRCode("test1", null, null));
        qrList.add(new QRCode("test", null, null));
        qrList.add(new QRCode("test1", null, null));

        qrGrid = findViewById(R.id.qr_list_grid);
        qrListAdapter = new QRListAdapter(this, qrList);
        qrGrid.setAdapter(qrListAdapter);

        ActivityResultLauncher<Intent> infoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {}
                });

        qrGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QRListActivity.this, QRInfoActivity.class);
                intent.putExtra("qr", qrListAdapter.getItem(i));
                intent.putExtra("user", user);
                infoLauncher.launch(intent);
            }
        });

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
