package com.example.qr_rangers;
import java.io.Serializable;

/**
 * Represents the location of a user or QR code
 * @author Adamya Nagpal
 */
public class Location implements Serializable {
    private double longitude;
    private double latitude;

    /**
     * constructs a location object with longitude and latitude info
     * @param longitude the longitude of the coordinate
     * @param latitude  the latitude of the coordinate
     */
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location() {
        this.longitude = 0;
        this.latitude = 0;
    }

    /**
     * Getter for longitude
     * @return Double longitude of location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for latitude
     * @return Double latitude of location
     */
    public double getLatitude() {
        return latitude;
    }

    /***
     * Returns the distance between this and loc
     * @param loc
     * location to get the distance from
     * @return
     *      distance between this and supplied point
     */
    public double getDistance(Location loc){
        android.location.Location locAndroid = new android.location.Location("loc");
        locAndroid.setLatitude(loc.getLatitude());
        locAndroid.setLongitude(loc.getLongitude());

        android.location.Location thisAndroid = new android.location.Location("loc2");
        thisAndroid.setLatitude(getLatitude());
        thisAndroid.setLongitude(getLongitude());
        return thisAndroid.distanceTo(locAndroid);
    }
}
