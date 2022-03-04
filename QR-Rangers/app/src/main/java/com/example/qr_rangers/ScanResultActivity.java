package com.example.qr_rangers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    private TextView Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Score = findViewById(R.id.textViewScore);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        QRCode qr = new QRCode(content,null,null);
        int score = qr.getScore();
        Score.setText(String.valueOf(score));



    }
}