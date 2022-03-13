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
}
