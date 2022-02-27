package com.example.qr_rangers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.NoSuchElementException;

/**
 * Represents a collection within Firestore along with some helper methods
 * @param <T>
 *     The type of document stored in the collection
 */
public class DbCollection<T extends DbDocument> {
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
        Task<DocumentSnapshot> documentSnapshotTask = collection.document(id).get();
        while (!documentSnapshotTask.isComplete());
        DocumentSnapshot doc = documentSnapshotTask.getResult();
        if (doc != null) {
            return (T) doc.toObject(DbDocument.class);
        }
        return null;
    }

    /**
     * Updates a document within the Firestore
     * @param data
     *  The new data for the document
     * @return
     *  Returns the document within Firestore after the operation
     */
    public T update(T data) {
        if (data.id == null || data.id.isEmpty()) {
            throw new IllegalArgumentException("Id not provided");
        }
        DocumentReference document = collection.document(data.id);
        if (document == null) {
            throw new NoSuchElementException(String.format("Document with id %s does not exist in collection %s", data.id, collection.getPath()));
        }
        Task<Void> t = document.set(data);
        while (!t.isComplete());
        t.getResult();

        // get the updated doc
        Task<DocumentSnapshot> documentSnapshotTask = collection.document(data.id).get();
        while(documentSnapshotTask.isComplete());
        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();

        if (documentSnapshot != null) {
            return (T) documentSnapshot.toObject(DbDocument.class);
        }
        return null;
    }

    /**
     * Adds a new document to the collection
     * @param data
     *  The document to add
     * @return
     *  Returns the document with generated id within Firestore
     */
    public T add(T data) {
        data.id = null;
        Task<DocumentReference> addTask = collection.add(data);

        while(!addTask.isComplete());
        Task<DocumentSnapshot> docTask = addTask.getResult().get();
        while(!docTask.isComplete());
        DocumentSnapshot doc = docTask.getResult()
;        if (doc != null) {
            return (T) doc.toObject(DbDocument.class);
        }
        return null;
    }

    /**
     * Deletes a specified document within the collection if it exists
     * @param id
     *  The generated id of the document to delete
     * @return
     *  Returns the task for deleting the document
     */
    public Task<Void> delete(String id) {
        T data = getById(id);
        if (data == null) {
            throw new NoSuchElementException(String.format("No such document with id %s", id));
        }
        return collection.document(id).delete();
    }
}

