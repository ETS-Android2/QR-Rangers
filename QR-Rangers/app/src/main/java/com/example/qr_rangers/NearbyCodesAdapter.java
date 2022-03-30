package com.example.qr_rangers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * An adapter for the nearby codes grid.
 * @author Alexander Salm
 * @version 1.0
 */
public class NearbyCodesAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> qrList;
    private Context context;
    private Location location;

    public NearbyCodesAdapter(Context context, ArrayList<QRCode> qrList) {
        super(context, 0, qrList);
        this.qrList = qrList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.nearby_qr_code, parent, false);
        }

        QRCode qr = qrList.get(position);

        ImageView qrImage = view.findViewById(R.id.nearby_code_image);
        TextView qrScore = view.findViewById(R.id.nearby_code_points);
        TextView qrDistance = view.findViewById(R.id.nearby_codes_distance);

        String scoreText = Integer.toString(qr.getScore()) + " pts.";
        qrScore.setText(scoreText);

        String distanceText;

        if (qr.getLocation() != null) {

            if (location == null) {

                GpsTracker tracker = new GpsTracker(context);
                if (!tracker.canGetLocation()) {
                    tracker.showSettingsAlert();
                    Log.i("NOTE", "Could not get location...");
                }

                location = new Location(tracker.getLocation().getLongitude(), tracker.getLocation().getLatitude());
                Log.i("NOTE", "Got location: " + Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
            }

            distanceText = round(location.getDistance(qr.getLocation())) + "m\naway";

            Log.i("NOTE", "QR LOCATION: " + Double.toString(qr.getLocation().getLatitude()) + " " + Double.toString(qr.getLocation().getLongitude()));
        }
        else{
            distanceText = "NONE";
        }
        qrDistance.setText(distanceText);
        qrScore.setText(scoreText);

        if (qr.getPhoto() != null) {
            byte[] imageBits = android.util.Base64.decode(qr.getPhoto(), Base64.DEFAULT);
            Bitmap bitImage = BitmapFactory.decodeByteArray(imageBits, 0, imageBits.length);
            qrImage.setImageBitmap(bitImage);
        } else {
            qrImage.setImageResource(R.drawable.ic_launcher_background);
        }

        return view;
    }

    /***
     * Rounds a double to the desired amount
     * @param num
     * number to round
     * @return
     *      rounded number
     */
    private String round(double num){
        DecimalFormat df = new DecimalFormat(".#");
        return df.format(num);
    }
}
