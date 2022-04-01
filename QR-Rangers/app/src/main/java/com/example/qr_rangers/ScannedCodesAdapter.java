package com.example.qr_rangers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Acn adapter for the a customer array for the User QR Code gallery.
 * @author Ronan Sandoval
 * @version 1.0
 */
public class ScannedCodesAdapter extends ArrayAdapter<ScannedCode> {
    private ArrayList<ScannedCode> scannedCodes;
    private Context context;

    public ScannedCodesAdapter(Context context, ArrayList<ScannedCode> scannedCodes) {
        super(context, 0, scannedCodes);
        this.scannedCodes = scannedCodes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.qr_item, parent, false);
        }

        ScannedCode qr = scannedCodes.get(position);

        ImageView qrImage = view.findViewById(R.id.qr_item_image);
        TextView qrScore = view.findViewById(R.id.qr_item_points);

        String scoreText = Integer.toString(qr.getCode().getScore()) + " pts.";
        qrScore.setText(scoreText);

        if (qr.getPhoto() != null) {
            byte[] imageBits = Base64.decode(qr.getPhoto(), Base64.DEFAULT);
            Bitmap bitImage = BitmapFactory.decodeByteArray(imageBits, 0, imageBits.length);
            qrImage.setImageBitmap(bitImage);
        } else {
            qrImage.setImageResource(R.drawable.ic_launcher_background);
        }

        return view;
    }
}
