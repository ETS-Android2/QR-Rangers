package com.example.qr_rangers;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Base64;

/**
 * Acn adapter for the a customer array for the User QR Code gallery.
 * @author Ronan Sandoval
 * @version 1.0
 */
public class QRListAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> qrList;
    private Context context;

    public QRListAdapter(Context context, ArrayList<QRCode> qrList) {
        super(context, 0, qrList);
        this.qrList = qrList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.qr_item, parent, false);
        }

        QRCode qr = qrList.get(position);

        ImageView qrImage = view.findViewById(R.id.qr_item_image);
        TextView qrScore = view.findViewById(R.id.qr_item_points);

        String scoreText = Integer.toString(qr.getScore()) + " pts.";
        qrScore.setText(scoreText);

        //TODO: image needs to be decoded
        qrImage.setImageResource(R.drawable.ic_launcher_background);
        //qrImage.setImageResource(BitmapFactory.qr.getPhoto());

        return view;
    }
}
