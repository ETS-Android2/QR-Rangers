package com.example.qr_rangers;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.NoSuchElementException;

/**
 * Represents a collection within Firestore along with some helper methods
 * @param <T>
 *     The type of document stored in the collection
 */
public class DbCollection<T> {
    private CollectionReference collection;

    /**
     * Constructor for the DbCollection
     * @param collectionName
     *  The name of the collection within Firestore
     */
    public DbCollection(String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.collection = db.collection(collectionName);
    }

    /**
     * Gets a document from the collection by its generated id
     * @param id
     *  The generated id of the document within Firestore
     * @return
     *  Returns the document if found or null
     */
    public T getById(String id) {
        return (T) collection.document(id);
    }

    /**
     * Updates a document within the Firestore
     * @param id
     *  The generated id of the document within Firestore
     * @param data
     *  The new data for the document
     * @return
     *  Returns the document within Firestore after the operation
     */
    public T update(String id, T data) {
        DocumentReference document = collection.document(id);
        if (document == null) {
            throw new NoSuchElementException(String.format("Document with id %s does not exist in collection %s", id, collection.getPath()));
        }
        Task t = document.set(data);
        t.getResult();
        return (T) collection.document(id);
    }
}

