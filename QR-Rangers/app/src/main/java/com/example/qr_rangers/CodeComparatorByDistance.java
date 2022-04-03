package com.example.qr_rangers;

import java.util.Comparator;

/**
 * A command class for comparing the locations of QR Codes
 */
public class CodeComparatorByDistance implements Comparator<QRCode> {
    private Location location;
    public CodeComparatorByDistance(Location location){
        this.location = location;
    }

    /**
     * Determines which location distance of a QR code is closer
     * @param a - First QR code to compare
     * @param b - Second QR code to compare
     * @return 1 if b is closer, -1 if a is closer, 0 if both are null.
     */
    @Override
    public int compare(QRCode a, QRCode b){
        if (a.getLocation() == null && b.getLocation() == null) return 0;
        if (a.getLocation() == null) return 1;
        if (b.getLocation() == null) return -1;

        return location.getDistance(a.getLocation()) > location.getDistance(b.getLocation()) ? 1 : -1;
    }
}
