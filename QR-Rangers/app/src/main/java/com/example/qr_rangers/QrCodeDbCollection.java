package com.example.qr_rangers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Gives access to the QRCode collection in the Firestore database
 */
public class QrCodeDbCollection implements IDbCollection<QRCode> {
    private CollectionReference collection;

    public QrCodeDbCollection(CollectionReference collection) {
        this.collection = collection;
    }
    /**
     * Gets a QRCode by its generated id if it exists
     *
     * @param id The generated id of the QRCode
     * @return Returns the QRCode if it exists or null
     */
    @Override
    public QRCode getById(String id) {
        if (id == null) {
            return null;
        }
        Task<DocumentSnapshot> documentSnapshotTask = collection.document(id).get();
        while (!documentSnapshotTask.isComplete());
        DocumentSnapshot doc = documentSnapshotTask.getResult();
        if (doc.getData() != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return QRCode.fromMap(map);
        }
        return null;
    }

    /**
     * Gets a QRCode by its name field if it exists
     *
     * @param name The hash of the QRCode to search for
     * @return Returns the QRCode if it exists or null
     */
    @Override
    public QRCode getByName(String name) {
        ArrayList<User> result = new ArrayList<>();
        Task<QuerySnapshot> task = collection.whereEqualTo("codeInfo", name).get();
        while(!task.isComplete());
        QuerySnapshot snap = task.getResult();
        List<DocumentSnapshot> docs = snap.getDocuments();
        if (docs.size() == 0) {
            return null;
        }
        DocumentSnapshot doc = docs.get(0);
        if (doc.getData() != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return QRCode.fromMap(map);
        }
        return null;
    }

    /**
     * Gets all QRCodes within the collection
     *
     * @return Returns a list of all QRCodes within the collection
     */
    @Override
    public ArrayList<QRCode> getAll() {
        Task<QuerySnapshot> task = collection.get();
        while(!task.isComplete());
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        ArrayList<QRCode> result = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            if (doc.getData() != null) {
                Map<String, Object> map = doc.getData();
                map.put("id", doc.getId());
                result.add(QRCode.fromMap(map));
            }
        }

        return result;
    }

    /**
     * Updates a QRCode within the collection
     *
     * @param data The new data for the QRCode
     * @return Returns the QRCode within collection after the operation
     */
    @Override
    public QRCode update(QRCode data) {
        if (data.getId() == null || data.getId().isEmpty()) {
            throw new IllegalArgumentException("Id not provided");
        }
        DocumentReference document = collection.document(data.getId());
        if (document == null) {
            throw new NoSuchElementException(String.format("Document with id %s does not exist in collection %s", data.getId(), collection.getPath()));
        }

        Map<String, Object> sanitizedData = data.toMap();
        Task<Void> t = document.set(sanitizedData);
        while (!t.isComplete());
        t.getResult();

        // Get the updated doc
        Task<DocumentSnapshot> documentSnapshotTask = collection.document(data.getId()).get();
        while(!documentSnapshotTask.isComplete());
        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();

        if (documentSnapshot != null) {
            Map<String, Object> map = documentSnapshot.getData();
            map.put("id", documentSnapshot.getId());
            return QRCode.fromMap(map);
        }
        return null;
    }

    /**
     * Adds a new QRCode to the collection
     *
     * @param data The QRCode to add
     * @return Returns the QRCode with generated id within collection
     */
    @Override
    public QRCode add(QRCode data) {
        Map<String, Object> sanitizedData = data.toMap();
        Task<DocumentReference> addTask = collection.add(sanitizedData);

        while(!addTask.isComplete());
        Task<DocumentSnapshot> docTask = addTask.getResult().get();
        while(!docTask.isComplete());
        DocumentSnapshot doc = docTask.getResult();
        if (doc != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return QRCode.fromMap(map);
        }
        return null;
    }

    /**
     * Deletes a specified QRCode within the collection if it exists
     *
     * @param id The id of the QRCode to delete
     * @return Returns the task for deleting the QRCode
     */
    @Override
    public Task<Void> delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be provided.");
        }
        Task<DocumentSnapshot> dataTask = collection.document(id).get();
        while (!dataTask.isComplete());
        DocumentSnapshot data = dataTask.getResult();
        if (data.getData() == null) {
            throw new NoSuchElementException(String.format("No such document with id %s", id));
        }
        ArrayList<String> scannedCodeIds = (ArrayList<String>) data.get("scannedCodes");
        if (scannedCodeIds != null) {
            for (String codeId : scannedCodeIds) {
                Database.ScannedCodes.deleteFromQR(codeId);
            }
        }
        return collection.document(id).delete();
    }

    /**
     * Checks if a QRCode with the specified hash exists within the collection
     *
     * @param name The hash of the QRCode to search for
     * @return Returns whether the document exists or not
     */
    @Override
    public boolean existsName(String name) {
        return this.getByName(name) != null;
    }

    /**
     * Checks if a QRCode with the specified id exists within the collection
     *
     * @param id The id of the QRCode to search for
     * @return Returns whether the QRCode exists or not
     */
    @Override
    public boolean existsId(String id) {
        return this.getById(id) != null;
    }
}
