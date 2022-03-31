package com.example.qr_rangers;

import java.util.Comparator;

public class CodeComparatorByDistance implements Comparator<QRCode> {
    private Location location;
    public CodeComparatorByDistance(Location location){
        this.location = location;
    }

    @Override
    public int compare(QRCode a, QRCode b){
        if (a.getLocation() == null && b.getLocation() == null) return 0;
        if (a.getLocation() == null) return 1;
        if (b.getLocation() == null) return -1;

        return location.getDistance(a.getLocation()) > location.getDistance(b.getLocation()) ? 1 : -1;
    }
}
