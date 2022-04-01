package com.example.qr_rangers;
import java.io.Serializable;

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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
