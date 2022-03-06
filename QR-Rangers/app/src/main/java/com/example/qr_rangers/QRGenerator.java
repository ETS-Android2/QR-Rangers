package com.example.qr_rangers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This is a class that, given a username, can generate a QR code to represent that user
 * @author Alexander Salm
 * @version 1.0
 */
public class QRGenerator {
    private final User user;

    /**
     * Constructs a QRGenerator object
     *
     * @param user The user that needs a qr code generated
     */
    public QRGenerator(User user){
        this.user = user;
    }

    /**
     * Returns the current user associated with this qr generator
     *
     * @return Returns the generated QR Code
     */
    public User getUser(){
        return user;
    }

    public Bitmap getQrCode(){
        //find the width and height of the screen in pixels
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        // generating dimension from width and height. (smaller of the 2)
        int smaller = width < height ? width : height;
        int dimen = smaller * 3 / 4;

        QRGEncoder encoder = new QRGEncoder(user.getId(), null, QRGContents.Type.TEXT, dimen);
        try {
            return encoder.encodeAsBitmap();
        } catch (WriterException e) {
            Log.e("ERROR CONVERTING QR CODE!", e.toString());
            return null;
        }
    }
}
