package com.example.qr_rangers;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An object that represents a QR Code object, containing its score, information,
 * photo, comment and location
 *
 * @author Jawdat
 * @version 1.0.2
 */
public class QRCode extends DbDocument implements Serializable {
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
     * Constructor for QR codes that need their info to be replaced with the hash for privacy reasons
     */
    QRCode(String /*temp, QRCode*/ info, @Nullable String photo, @Nullable Location location,boolean hideInfo){
        QRScore qrScore = new QRScore();
        codeInfo = info;
        score = qrScore.calculateScore(this);
        if (hideInfo)
        {
            codeInfo = qrScore.convertToSHA256(this).substring(0,10);
        }
        if(!Objects.isNull(photo)){
            this.photo = photo; // temp
        }

        if(!Objects.isNull(location)){
            this.location = location; // temp
        //CODE TO ADD THE ROUNDED COORDS TO HASH
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            latitude = (double) Math.round(latitude * 100) / 100;
            longitude = (double) Math.round(longitude * 100) / 100;
            String latitudeAsString = String.valueOf(latitude);
            String longitudeAsString = String.valueOf(longitude);
            if(latitude>0)
                latitudeAsString="+" + latitudeAsString;
            if(longitude>0)
                longitudeAsString="+" + longitudeAsString;
            codeInfo = codeInfo.concat(latitudeAsString).concat(longitudeAsString);
        }

    }

    /**
     * Empty constructor for use with getting QRCodes from the db
     */
    public QRCode() {}


    /**
     * Getter for the QR code data
     *
     * @return
     *      String representing the QR Code data
     */
    public String getCodeInfo() {
        return codeInfo;
    }

    /**
     * Setter for the QR code data
     *
     * @param codeInfo
     *      String to set the QR Code data to
     */
    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

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
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
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

    /**
     * Creates a new QrCode object from a mapped representation
     * @param map
     *      The map containing the values for the object
     * @return
     *      Returns the created QrCode object
     */
    public static QRCode fromMap(Map<String, Object> map) {
        Map<String, Object> locMap = (Map<String, Object>) map.get("location");
        QRCode qrCode = new QRCode((String) map.get("codeInfo"), (String) map.get("photo"),null);
        qrCode.setScore(Math.toIntExact((Long) map.get("score")));
        if (locMap != null) {
            qrCode.setLocation(new Location((double) locMap.get("longitude"), (double) locMap.get("latitude")));
        }
        qrCode.setId((String) map.get("id"));
        // TODO: Add in setting score values from the map once setters are complete
        return qrCode;
    }

    /**
     * Converts the QrCode into a map representation for inserting into the Firestore
     * @return
     *      Returns the map representation of the object
     */
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
