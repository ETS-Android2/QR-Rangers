package com.example.qr_rangers;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Search {
    private DbCollection db;

    Search(){
        db = new DbCollection("users");
    }

    public User FindUser(String username){
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
}
