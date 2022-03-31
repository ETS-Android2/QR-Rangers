package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.ByteArrayOutputStream;

/**
 * Activity that is shown after a valid scan
 * Enables the user to add location info and a photo to the QR Code before adding it
 */
//NOTE: currently displays a dummy value for number of other people who scanned this code
//TODO: make the number of other users who scanned this code accurate
public class ScanResultActivity extends AppCompatActivity {

    private TextView totalScore;
    private SwitchMaterial attachLocation;
    //private ImageView imgPreview;
    Bitmap photo = null;
    private static final int pic_id = 123;
    private Location location = null;
    private GpsTracker gpsTracker;
    private User user;
    private QRCode qr;
    private ScannedCode code;

    private ImageButton cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        qr = Database.QrCodes.getByName(content);
        if (qr == null) {
            qr = Database.QrCodes.add(new QRCode(content, null));
        }
        user = loadUser();
        code = new ScannedCode(qr, user, null, null, null);
        int score = qr.getScore();
        TextView scoreTextView = findViewById(R.id.textViewScore);
        scoreTextView.setText(String.valueOf(score).concat(" pts."));
        totalScore = findViewById(R.id.newtotalscore);
        totalScore.setText(String.valueOf(intent.getStringExtra("totalScore")));
        TextView codeUserCount = findViewById(R.id.codeusercount);
        codeUserCount.setText("" + qr.getScannedCount());
        EditText commentBox = findViewById(R.id.commentbox);
        gpsTracker = new GpsTracker(ScanResultActivity.this);
        if(!gpsTracker.canGetLocation())
            gpsTracker.showSettingsAlert();
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

        //Saving the QR code to server happens here

        Button saveButton = findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disable the camera button when saving qr code to db
                cameraButton.setClickable(false);
                //process location data if provided by the user
                if (attachLocation.isChecked()){
                    if(gpsTracker.canGetLocation()){
                        double longitude = gpsTracker.getLongitude();
                        double latitude = gpsTracker.getLatitude();
                        gpsTracker.stopUsingGPS();
                        location = new Location(longitude,latitude);
                    }
                }
                //Process image to JPEG if provided by user
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                String imageEncoded = null;
                if (photo != null) {
                    photo.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                }
                //generate code for saving to db from collected data
                ScannedCode codeToSave = new ScannedCode(qr,user,location,commentBox.getText().toString(),imageEncoded);
                if (user.HasQR(codeToSave)){
                    Toast.makeText(getBaseContext(), "You already scanned this one!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        boolean qrChanged = false;
                        if (qr.getLocation() == null && location != null) {
                            qr.setLocation(location);
                            qrChanged = true;
                        }
                        if (qr.getPhoto() == null && photo != null) {
                            qr.setPhoto(photo.toString());
                            qrChanged = true;
                        }
                        if (qrChanged) {
                            Database.QrCodes.update(qr);
                        }
                        user.AddQR(codeToSave);
                        Database.Users.update(user);
                    }
                    catch (Exception e) {
                        System.out.println(e.toString());
                        Toast.makeText(ScanResultActivity.this,"exception encountered".concat(e.toString()),Toast.LENGTH_SHORT).show();
                    }
                }
             finish();
            }
        });

    }

    /**
     * gets the image from the camera API
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && resultCode == Activity.RESULT_OK) {
             photo = (Bitmap) data.getExtras().get("data");
             cameraButton.setImageBitmap(photo);
        }
    }

    /**
     * Fetches user from database
     * @return user from db
     */
    private User loadUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sharedPreferences.getString("ID", null);
        User localUser = Database.Users.getById(id);
        return localUser;
    }
/*
    Should use this when we develop functionality to display the QR code
    public static Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
 */
}