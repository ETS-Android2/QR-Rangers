package com.example.qr_rangers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 * Activity to view profile stats, access gallery, and obtain user code
 * @author Ronan Sandoval, Alexander Salm
 * @version 1.1
 */
public class ProfileActivity extends AppCompatActivity{

    private TextView nameText;
    private TextView totalScoreText;
    private TextView codesScannedText;
    private TextView scoreRankingText;
    private TextView scanRankingText;
    private TextView bestQRRankingText;

    private Button viewGalleryButton;
    private Button shareProfileButton;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private User user;
    private Boolean isMyAccount;

    private final float fadeSpeed = (float)1.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");
        isMyAccount = getIntent().getBooleanExtra("isMyAccount", false);

        nameText = findViewById(R.id.profilename);
        nameText.setText(user.getUsername());

        totalScoreText = findViewById(R.id.totalscore);
        String totalScore = String.valueOf(user.getTotalScore()) + " pts.";
        totalScoreText.setText(totalScore);

        codesScannedText = findViewById(R.id.codesscanned);
        String codesScanned = String.valueOf(user.getQRNum());
        codesScannedText.setText(String.valueOf(codesScanned));

        scoreRankingText = findViewById(R.id.scoreranking);
        String rankScore = String.valueOf(user.getUserRanks().getTotalScoreRank());
        scoreRankingText.setText("#" + rankScore);

        scanRankingText = findViewById(R.id.scanranking);
        String rankAmount = String.valueOf(user.getUserRanks().getQRScannedRank());
        scanRankingText.setText("#" + rankAmount);

        bestQRRankingText = findViewById(R.id.bestqrranking);
        String rankBest = String.valueOf(user.getUserRanks().getBestQRRank());
        bestQRRankingText.setText("#" + rankBest);



        ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    user = (User) result.getData().getSerializableExtra("user");
                }
            });

        viewGalleryButton = findViewById(R.id.viewgallerybutton);
        viewGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, QRListActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("isMyAccount", isMyAccount);
                galleryLauncher.launch(intent);
            }
        });

        shareProfileButton = findViewById(R.id.shareprofilebutton);

        if (!isMyAccount) {
            shareProfileButton.setVisibility(View.INVISIBLE);
        }

        shareProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap code = new QRGenerator(user).getQrCode();
                new ViewQrCodeFragment(code).show(getSupportFragmentManager(), "View_QR_Code");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        nameText = findViewById(R.id.profilename);
        nameText.setText(user.getUsername());

        totalScoreText = findViewById(R.id.totalscore);
        String totalScore = String.valueOf(user.getTotalScore()) + " pts.";
        totalScoreText.setText(totalScore);

        codesScannedText = findViewById(R.id.codesscanned);
        String codesScanned = String.valueOf(user.getQRNum());
        codesScannedText.setText(String.valueOf(codesScanned));

        scoreRankingText = findViewById(R.id.scoreranking);
        String rankScore = "#" + String.valueOf(user.getUserRanks().getTotalScoreRank());
        scoreRankingText.setText(rankScore);

        scanRankingText = findViewById(R.id.scanranking);
        String rankAmount = "#" + String.valueOf(user.getUserRanks().getQRScannedRank());
        scanRankingText.setText(rankAmount);

        bestQRRankingText = findViewById(R.id.bestqrranking);
        String rankBest = String.valueOf(user.getUserRanks().getBestQRRank());
        bestQRRankingText.setText("#" + rankBest);
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