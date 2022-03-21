package com.example.qr_rangers;

import java.io.Serializable;
import java.util.HashMap;

/**
 * An object that represents the ranks of a user
 *
 * @author Ryan
 * @version 1.0
 */
public class Rankings implements Serializable {
    private int totalScoreRank;
    private int bestQRRank;
    private int QRScannedRank;


    /**
     * Constructs a rank object
     */
    public Rankings() {
        this.totalScoreRank = -1;
        this.bestQRRank = -1;
        this.QRScannedRank = -1;
    }

    /**
     * Gets the total score rank
     *
     * @return
     *      An int that represents the rank of the users total points score
     */
    public int getTotalScoreRank() {
        return totalScoreRank;
    }

    /**
     * Sets the total score rank
     *
     * @param totalScoreRank
     *      The new rank value for the user
     */
    public void setTotalScoreRank(int totalScoreRank) {
        this.totalScoreRank = totalScoreRank;
    }

    /**
     * Gets the best unique QR code rank
     *
     * @return
     *      An int that represents the rank of the best unique QR code scanned by the user
     */
    public int getBestQRRank() {
        return bestQRRank;
    }

    /**
     * Sets the best unique QR code rank
     *
     * @param bestQRRank
     *      The new rank value for the user
     */
    public void setBestQRRank(int bestQRRank) {
        this.bestQRRank = bestQRRank;
    }

    /**
     * Gets the QR codes scanned rank
     *
     * @return
     *      An int that represents the rank of the amount of QR Codes scanned rank
     */
    public int getQRScannedRank() {
        return QRScannedRank;
    }

    /**
     * Sets the most QR codes scanned rank
     *
     * @param QRScannedRank
     *      The new rank value for the user
     */
    public void setQRScannedRank(int QRScannedRank) {
        this.QRScannedRank = QRScannedRank;
    }

    /**
     * Converts the Rankings into a map representation for inserting into the Firestore
     * @return
     *      Returns the map representation of the Rankings class
     */
    public HashMap toMap() {
        HashMap map = new HashMap();
        map.put("totalScoreRank", this.getTotalScoreRank());
        map.put("bestQRRank", this.getBestQRRank());
        map.put("QRScannedRank", this.getQRScannedRank());
        return map;
    }
}
