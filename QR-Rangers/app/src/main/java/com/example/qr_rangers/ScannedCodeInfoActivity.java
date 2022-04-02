package com.example.qr_rangers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

/**
 * Activity to that showcases information about a User's scanned QR Code
 * @author Ronan Sandoval
 * @version 1.1
 */
public class ScannedCodeInfoActivity extends AppCompatActivity {

    ScannedCode qr;
    User user;
    Boolean isMyAccount;

    TextView scannerText;
    TextView scannedByText;
    TextView scoreText;
    TextView commentText;
    ImageView image;

    Button deleteButton;
    Button viewMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        qr = (ScannedCode) getIntent().getSerializableExtra("qr");
        user = (User) getIntent().getSerializableExtra("user");
        isMyAccount = getIntent().getBooleanExtra("isMyAccount", false);

        scannerText = findViewById(R.id.qr_info_scanner);
        scannerText.setText(user.getUsername());
        scannedByText = findViewById(R.id.qr_info_others);
        scannedByText.setText(String.format("Scanned by %d other people", qr.getCode().getScannedCount() - 1));
        scoreText = findViewById(R.id.qr_info_points);
        String scoreString = qr.getCode().getScore() + " pts.";
        scoreText.setText(scoreString);
        commentText = findViewById(R.id.qr_info_comment);
        commentText.setText(qr.getComment());

        ListView commentsList = findViewById(R.id.qr_info_comment_list);
        commentsList.setVisibility(View.GONE);

        image = findViewById(R.id.qr_info_image);
        if (qr.getPhoto() != null) {
            byte[] imageBits = Base64.decode(qr.getPhoto(), Base64.DEFAULT);
            Bitmap bitImage = BitmapFactory.decodeByteArray(imageBits, 0, imageBits.length);
            image.setImageBitmap(bitImage);
        } else {
            image.setImageResource(R.drawable.ic_launcher_background);
        }

        deleteButton = findViewById(R.id.qr_info_delete);
        if (!isMyAccount) {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment deleteQRFragment = new DeleteScannedCodeConfirmationFragment(qr, user);
                deleteQRFragment.show(getSupportFragmentManager(), "Delete_QR");
            }
        });

        viewMapButton = findViewById(R.id.qr_info_view_map);
        viewMapButton.setOnClickListener(view -> {
            QRCode code = ScannedCodeInfoActivity.this.qr.getCode();
            Intent intent = new Intent(ScannedCodeInfoActivity.this, MapActivity.class);
            intent.putExtra("code", code);
            intent.putExtra("user", user);
            startActivity(intent);
        });
        viewMapButton.setVisibility(qr.getLocationScanned() == null ? View.INVISIBLE : View.VISIBLE);

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
