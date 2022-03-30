package com.example.qr_rangers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    Boolean isMyAccount;

    TextView scannedByText;
    TextView scoreText;
    TextView commentText;
    ImageView image;

    Button deleteButton;
    Button editCommentButton;
    Button viewMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        qr = (QRCode) getIntent().getSerializableExtra("qr");
        isMyAccount = getIntent().getBooleanExtra("isMyAccount", false);

//        scannerText = findViewById(R.id.qr_info_scanner);
//        scannerText.setText(user.getUsername());
        scannedByText = findViewById(R.id.qr_info_others);
        scannedByText.setText(String.format("Scanned by %d people", qr.getScannedCount()));
        scoreText = findViewById(R.id.qr_info_points);
        String scoreString = qr.getScore() + " pts.";
        scoreText.setText(scoreString);
        ArrayList<String> comments = qr.getComments();
        commentText = findViewById(R.id.qr_info_comment);
        // TODO: Make this a ListView
        if (comments.size() > 0) {
            commentText.setText(comments.get(0));
        }

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
                DialogFragment deleteQRFragment = new DeleteQRConfirmationFragment(qr);
                deleteQRFragment.show(getSupportFragmentManager(), "Delete_QR");
            }
        });

        editCommentButton = findViewById(R.id.qr_edit_comment);
        if (!isMyAccount) {
            editCommentButton.setVisibility(View.INVISIBLE);
        }

        viewMapButton = findViewById(R.id.qr_info_view_map);

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
