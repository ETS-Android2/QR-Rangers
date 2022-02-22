package com.example.qr_rangers;

import static java.util.Objects.isNull;

import android.location.Geocoder;
import android.media.Image;
import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class QRCode extends DbDocument{
    // REMINDER TO CHANGE .equals() DEPENDING ON codeInfo TYPE
    private int /*temp QRCode*/ codeInfo;
    private Image photo;
    private Geocoder location;
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
    QRCode(int /*temp, QRCode*/ info, @Nullable Image photo, @Nullable Geocoder location){
        score = calculateScore(info); // This is temp, will handled by another class
        if(!Objects.isNull(photo)){
            this.photo = photo; // temp
        }
        if(!Objects.isNull(location)){
            this.location = location; // temp
        }
    }

    /**
     * Calculates the score based on a QR Code's information
     * TEMPORARY, WILL BE HANDLED BY A DIFFERENT CLASS
     *
     * @param info
     *      The information of the QR Code that will be used to determine the score
     *
     * @return
     *      Returns an integer representation of the score of the QRCode
     */
    private int calculateScore(int /*temp QRCode*/ info){
        int score = 0; // change this later
        return score;
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
     * A getter function to get the image of the QR Code
     *
     * @return
     *      The image that was used for the QR Code
     */
    public Image getPhoto(){
        return photo;
    }

    /**
     * A setter function to set a new image for the QR Code
     *
     * @param photo
     *      The image that will be used for the QR Code
     */
    public void setPhoto(Image photo){
        this.photo = photo;
    }

    /**
     * A getter function to get the location of the QR Code
     *
     * @return
     *      The location that was used for the QR Code
     */
    public Geocoder getLocation(){
        return location;
    }

    /**
     * A setter function to set a new location for the QR Code
     *
     * @param location
     *      The new location that will be set for the QR Code
     */
    public void setLocation(Geocoder location){
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
        return this.codeInfo == ((QRCode) code).codeInfo; // Possibly change to .equals() depending on what codeInfo ends up being
    }
}
