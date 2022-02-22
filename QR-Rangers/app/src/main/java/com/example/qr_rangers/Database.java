package com.example.qr_rangers;

/**
 * Helper class that connects with the Firestore
 */
public class Database {
    public static DbCollection<User> Users = new DbCollection<>("users");
}
