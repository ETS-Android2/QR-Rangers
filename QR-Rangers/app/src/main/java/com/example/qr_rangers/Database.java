package com.example.qr_rangers;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Helper class that connects with the Firestore
 */
public class Database {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static UserDbCollection Users = new UserDbCollection(db.collection("users"), db.collection("admins"));
    public static QrCodeDbCollection QrCodes = new QrCodeDbCollection(db.collection("qrCodes"));
    public static AdminDbCollection Admins = new AdminDbCollection(db.collection("admins"));
}
