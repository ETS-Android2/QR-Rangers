package com.example.qr_rangers;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
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
    private String codeInfo;
    private ArrayList<ScannedCode> scannedCodes;
    private int score;
    private Location location;

    /**
     * Initializes the QRCode object for display and comparison
     *
     * @param info
     *      Contains the QR Code information that will be used to calculate score and determine what QRCode it was
     * @param location
     *      Contains the Geological location of the QRCode
     */
    @RequiresApi(api = Build.VERSION_CODES.N) // I don't really know what to do about this
    QRCode(String /*temp, QRCode*/ info, @Nullable Location location){
        QRScore qrScore = new QRScore();
        codeInfo = info;
        score = qrScore.calculateScore(this);
        if(!Objects.isNull(location)){
            this.location = location; // temp
        }

    }

    /**
     * Constructor for QR codes that need their info to be replaced with the hash for privacy reasons
     */
    QRCode(String /*temp, QRCode*/ info, @Nullable Location location, boolean hideInfo){
        QRScore qrScore = new QRScore();
        codeInfo = info;
        score = qrScore.calculateScore(this);
        if (hideInfo)
        {
            codeInfo = qrScore.convertToSHA256(this).substring(0,10);
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

    public ArrayList<ScannedCode> getScannedCodes() {
        return this.scannedCodes;
    }

    public void setScannedCodes(ArrayList<ScannedCode> scannedCodes) {
        this.scannedCodes = scannedCodes;
    }

    public void addScannedCode(ScannedCode scannedCode) {
        this.scannedCodes.add(scannedCode);
    }

    public void deleteScannedCode(ScannedCode scannedCode) {
        this.scannedCodes.remove(scannedCode);
    }

    public ArrayList<String> getComments() {
        ArrayList<String> comments = new ArrayList<>();
        for (ScannedCode code : this.scannedCodes) {
            comments.add(code.getComment());
        }
        return comments;
    }

    public ArrayList<String> getPictures() {
        ArrayList<String> pictures = new ArrayList<>();
        for (ScannedCode code : this.scannedCodes) {
            pictures.add(code.getPhoto());
        }
        return pictures;
    }

    public ArrayList<Location> getLocationsScanned() {
        ArrayList<Location> locations = new ArrayList<>();
        for (ScannedCode code : this.scannedCodes) {
            locations.add(code.getLocationScanned());
        }
        return locations;
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
        QRCode qrCode = new QRCode((String) map.get("codeInfo"), null, false);
        if (locMap != null) {
            qrCode.setLocation(new Location((double) locMap.get("longitude"), (double) locMap.get("latitude")));
        }
        qrCode.setScore(Math.toIntExact((Long) map.get("score")));
        qrCode.setId((String) map.get("id"));
        ArrayList<ScannedCode> scannedCodes = new ArrayList<>();
        ArrayList<String> codeIds = (ArrayList<String>) map.get("scannedCodes");
        if (codeIds == null) {
            codeIds = new ArrayList<>();
        }
        for (String id : codeIds) {
            scannedCodes.add(Database.ScannedCodes.getById(id));
        }
        qrCode.setScannedCodes(scannedCodes);
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
        map.put("score", this.getScore());
        ArrayList<String> scannedCodes = new ArrayList<>();
        for (ScannedCode code : this.scannedCodes) {
            scannedCodes.add(code.getId());
        }
        map.put("scannedCodes", scannedCodes);
        return map;
    }
    @Override
    public String toString(){
        return this.codeInfo;
    }

}
