package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ScanResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private ImageButton cameraButton;
    private Switch attachLocation;
    Bitmap photo = null;
    private Button saveButton;
    private static final int pic_id = 123;
    private Location location = null;
    private GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        QRCode qr = new QRCode(content,null,null);
        int score = qr.getScore();
        scoreTextView = findViewById(R.id.textViewScore);
        scoreTextView.setText(String.valueOf(score).concat(" pts."));
        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });
        attachLocation = findViewById(R.id.locationswitch);
        saveButton = findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachLocation.isChecked()){
                    gpsTracker = new GpsTracker(ScanResultActivity.this);
                    if(gpsTracker.canGetLocation()){
                        double longitude = gpsTracker.getLongitude();
                        double latitude = gpsTracker.getLatitude();
                        gpsTracker.stopUsingGPS();
                        location = new Location(longitude,latitude);
                    }
                    else{
                        gpsTracker.showSettingsAlert();
                    }
                }
                QRCode QrToSave = new QRCode(content,photo,location);

            }
        });

    }
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && resultCode == Activity.RESULT_OK) {
             photo = (Bitmap) data.getExtras().get("data");
             //photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }


    }
}