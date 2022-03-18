package com.example.qr_rangers;

import java.io.Serializable;
import java.util.HashMap;

public class Rankings implements Serializable {
    private int totalScoreRank;
    private int bestQRRank;
    private int QRScannedRank;

    public Rankings() {
        this.totalScoreRank = -1;
        this.bestQRRank = -1;
        this.QRScannedRank = -1;
    }

    public int getTotalScoreRank() {
        return totalScoreRank;
    }

    public void setTotalScoreRank(int totalScoreRank) {
        this.totalScoreRank = totalScoreRank;
    }

    public int getBestQRRank() {
        return bestQRRank;
    }

    public void setBestQRRank(int bestQRRank) {
        this.bestQRRank = bestQRRank;
    }

    public int getQRScannedRank() {
        return QRScannedRank;
    }

    public void setQRScannedRank(int QRScannedRank) {
        this.QRScannedRank = QRScannedRank;
    }

    public HashMap toMap() {
        HashMap map = new HashMap();
        map.put("totalScoreRank", this.getTotalScoreRank());
        map.put("bestQRRank", this.getBestQRRank());
        map.put("QRScannedRank", this.getQRScannedRank());
        return map;
    }
}
