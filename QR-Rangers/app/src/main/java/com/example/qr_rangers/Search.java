package com.example.qr_rangers;

import android.widget.ListAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private DbCollection db;

    Search(){
        db = new DbCollection("users");
    }

    /**
     * A function to return a User object corresponding with a username
     *
     * @param username
     *      The username that we want to find
     * @return
     *      Returns an ArrayList containing the User object corresponding with the username
     */
    public ArrayList<User> FindUser(String username){
        //final User[] result = {null};
//        db.collectionGroup("users").whereEqualTo("username", username).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    private User user;
//
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
//                        result[0] = queryDocumentSnapshots.toObjects(user.getClass()).get(0);
//                    }
//        });
//        return result[0];
        return db.FindUser(username);
    }

    /**
     * A function to check whether a username exists in the database
     *
     * @param username
     *      The username that we want to check for
     * @return
     *      A boolean, true if that username is being used, false otherwise
     */
    public boolean CheckUser(String username){
        return db.CheckUser(username);
    }
}