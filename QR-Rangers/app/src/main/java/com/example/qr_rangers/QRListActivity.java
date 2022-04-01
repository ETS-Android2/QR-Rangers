package com.example.qr_rangers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to that showcases a user's scanned QR codes
 * @author Ronan Sandoval
 * @version 1.0
 */
public class QRListActivity extends AppCompatActivity {
    TextView nameText;

    GridView qrGrid;
    ScannedCodesAdapter scannedCodesAdapter;

    User user;
    Boolean isMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_list);

        user = (User) getIntent().getSerializableExtra("user");
        isMyAccount = getIntent().getBooleanExtra("isMyAccount", false);

        nameText = findViewById(R.id.qr_list_name);
        String nameString = user.getUsername() + "'s Gallery";
        nameText.setText(nameString);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        qrGrid = findViewById(R.id.qr_list_grid);
        scannedCodesAdapter = new ScannedCodesAdapter(this, user.getQRList());
        qrGrid.setAdapter(scannedCodesAdapter);

        ActivityResultLauncher<Intent> infoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getData() != null) {
                            user = (User) result.getData().getSerializableExtra("user");
                            scannedCodesAdapter = new ScannedCodesAdapter(QRListActivity.this, user.getQRList());
                            qrGrid.setAdapter(scannedCodesAdapter);
                            scannedCodesAdapter.notifyDataSetChanged();
                        }

                    }
                });

        qrGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QRListActivity.this, ScannedCodeInfoActivity.class);
                intent.putExtra("qr", scannedCodesAdapter.getItem(i));
                intent.putExtra("user", user);
                intent.putExtra("isMyAccount", isMyAccount);
                infoLauncher.launch(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent resultUser = new Intent();
                resultUser.putExtra("user", user);
                this.setResult(Activity.RESULT_OK, resultUser);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent resultUser = new Intent();
        resultUser.putExtra("user", user);
        this.setResult(Activity.RESULT_OK, resultUser);
        this.finish();
    }
}
