package com.example.qr_rangers;

import android.graphics.Bitmap;
import android.location.Geocoder;
import android.media.Image;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.api.SystemParametersOrBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QRCode extends DbDocument{
    // REMINDER TO CHANGE .equals() DEPENDING ON codeInfo TYPE
    private String /*temp QRCode*/ codeInfo;
    private String photo;
    private Location location;
    private int score;
    private String comment; // I'm not sure if this should go here necessarily

    /**
     * Initializes the QRCode object for display and comparison
     *
     * @param photo
     *      Contains the image that will be used to represent the QRCode
     * @param info
     *      Contains the QR Code information that will be used to calculate score and determine what QRCode it was
     * @param location
     *      Contains the Geological location of the QRCode
     */
    @RequiresApi(api = Build.VERSION_CODES.N) // I don't really know what to do about this
    QRCode(String /*temp, QRCode*/ info, @Nullable String photo, @Nullable Location location){
        QRScore qrScore = new QRScore();
        codeInfo = info;
        score = qrScore.calculateScore(this);
        if(!Objects.isNull(photo)){
            this.photo = photo; // temp
        }
        if(!Objects.isNull(location)){
            this.location = location; // temp
        }

    }

    /**
     * Empty constructor for use with getting QRCodes from the db
     */
    public QRCode() {}

    /**
     * A getter function to get the score of the QR Code
     *
     * @return
     *      The score that was calculated for the QR Code
     */
    public int getScore(){
        return score;
    }

    /**
     * A getter function to get the image of the QR Code
     *
     * @return
     *      The URL to the image that was used for the QR Code
     */
    public String getPhoto(){
        return photo;
    }

    /**
     * A setter function to set a new image for the QR Code
     *
     * @param photo
     *      The URL to the image image that will be used for the QR Code
     */
    public void setPhoto(String photo){
        this.photo = photo;
    }

    /**
     * A getter function to get the location of the QR Code
     *
     * @return
     *      The location that was used for the QR Code
     */
    public Location getLocation(){
        return location;
    }

    /**
     * A setter function to set a new location for the QR Code
     *
     * @param location
     *      The new location that will be set for the QR Code
     */
    public void setLocation(Location location){
        this.location = location;
    }

    /**
     * An equals override function to set equivalence to being the same QR Code
     *
     * @param code
     *      Another QRCode object to compare to
     *
     * @return
     *      Returns true if the QRCode objects both contain the same QRCode, false otherwise
     *
     * @throws IllegalArgumentException
     *      If we are not comparing to a QRCode, this is a problem
     */
    @Override
    public boolean equals(Object code){
        if(!(code instanceof QRCode)){
            throw new IllegalArgumentException();
        }
        return this.codeInfo.equals(((QRCode) code).codeInfo);
    }

    @Override
    public DbDocument fromMap(Map<String, Object> map) {
        Map<String, Object> locMap = (Map<String, Object>) map.get("location");
        QRCode qrCode = new QRCode((String) map.get("codeInfo"), (String) map.get("photo"),null);
        if (locMap != null) {
            qrCode.setLocation(new Location((double) locMap.get("longitude"), (double) locMap.get("latitude")));
        }
        qrCode.setId((String) map.get("id"));
        // TODO: Add in setting score values from the map once setters are complete
        return qrCode;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        // TODO: Figure out if we want this
        map.put("codeInfo", this.codeInfo);
        map.put("photo", this.getPhoto());
        map.put("location", this.getLocation());
        map.put("score", this.getScore());
        map.put("comment", this.comment);
        return map;
    }
    @Override
    public String toString(){
        return this.codeInfo;
    }

}
