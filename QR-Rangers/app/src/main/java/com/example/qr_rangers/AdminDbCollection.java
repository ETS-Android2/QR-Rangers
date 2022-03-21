package com.example.qr_rangers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDbCollection {
    private CollectionReference collection;

    public AdminDbCollection(CollectionReference collection) {
        this.collection = collection;
    }

    /**
     * Checks if a given id is an admin id
     * @param id
     *      The id to check
     * @return
     *      Returns whether or not the given id is of an admin
     */
    public boolean isAdmin(String id) {
        ArrayList<String> ids = this.getAll();
        return ids.contains(id);
    }

    /**
     * Gets all admin ids within the collection
     * @return
     *      Returns a list of admin ids
     */
    public ArrayList<String> getAll() {
        Task<QuerySnapshot> task = collection.get();
        while(!task.isComplete());
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        ArrayList<String> result = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            if (doc.getData() != null) {
                Map<String, Object> map = doc.getData();
                result.add((String) map.get("id"));
            }
        }

        return result;
    }

}
