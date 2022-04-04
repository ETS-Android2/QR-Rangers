package com.example.qr_rangers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

/**
 * Activity to that showcases information about a User's scanned QR Code
 * @author Ronan Sandoval
 * @version 1.1
 */
public class QRInfoActivity extends AppCompatActivity {

    QRCode qr;
    User user;

    TextView scannerText;
    TextView scoreText;
    ListView commentList;
    ArrayAdapter<String> commentAdapter;
    ImageView image;

    Button deleteButton;
    Button viewMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_qr_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        qr = (QRCode) getIntent().getSerializableExtra("qr");
        user = (User) getIntent().getSerializableExtra("user");

        scannerText = findViewById(R.id.qr_general_info_scanner);
        scannerText.setText(String.format("%d people", qr.getScannedCount()));

        scoreText = findViewById(R.id.qr_general_info_points);
        String scoreString = qr.getScore() + " pts.";
        scoreText.setText(scoreString);
        ArrayList<String> comments = qr.getComments();

        commentList = findViewById(R.id.qr_general_info_comment_list);
        commentAdapter = new ArrayAdapter<String>(this, R.layout.comment, comments);
        commentList.setAdapter(commentAdapter);

        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QRInfoActivity.this, ScannedCodeInfoActivity.class);
                intent.putExtra("user", user);
                ScannedCode scan = null;
                ArrayList<ScannedCode> scans = qr.getScannedCodes();
                for(int x = 0; x < scans.size(); x++){
                    if(scans.get(i).getComment().equals(commentAdapter.getItem(i))){
                        scan = scans.get(i);
                        break;
                    }
                }
                intent.putExtra("qr", scan);
                if (scan != null) startActivity(intent);
            }
        });

        image = findViewById(R.id.qr_general_info_image);
        if (qr.getPhoto() != null) {
            byte[] imageBits = Base64.decode(qr.getPhoto(), Base64.DEFAULT);
            Bitmap bitImage = BitmapFactory.decodeByteArray(imageBits, 0, imageBits.length);
            image.setImageBitmap(bitImage);
        } else {
            image.setImageResource(R.drawable.ic_launcher_background);
        }

        deleteButton = findViewById(R.id.qr_general_info_delete);

        if (!Database.Admins.isAdmin(user.getId())) {
            deleteButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(view -> {
            DialogFragment deleteQRFragment = new DeleteQRConfirmationFragment(qr);
            deleteQRFragment.show(getSupportFragmentManager(), "Delete_QR");
        });

        viewMapButton = findViewById(R.id.qr_general_info_view_map);
        viewMapButton.setOnClickListener(view -> {
            Intent intent = new Intent(QRInfoActivity.this, MapActivity.class);
            intent.putExtra("code", QRInfoActivity.this.qr);
            intent.putExtra("user", QRInfoActivity.this.user);
            startActivity(intent);
        });

        viewMapButton.setVisibility(qr.getLocation() == null ? View.INVISIBLE : View.VISIBLE);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
