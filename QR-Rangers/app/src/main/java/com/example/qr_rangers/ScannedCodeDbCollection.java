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
 * Gives access to the ScannedCode collection in the Firestore database
 */
public class ScannedCodeDbCollection implements IDbCollection<ScannedCode> {
    private CollectionReference collection;

    public ScannedCodeDbCollection(CollectionReference collection) {
        this.collection = collection;
    }

    /**
     * Gets a ScannedCode by its generated id if it exists
     * @param id
     *      The generated id of the ScannedCode
     * @return
     *      Returns the ScannedCode if it exists or null
     */
    @Override
    public ScannedCode getById(String id) {
        if (id == null) {
            return null;
        }
        Task<DocumentSnapshot> documentSnapshotTask = collection.document(id).get();
        while (!documentSnapshotTask.isComplete());
        DocumentSnapshot doc = documentSnapshotTask.getResult();
        if (doc.getData() != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return ScannedCode.fromMap(map);
        }
        return null;
    }

    /**
     * @deprecated Use the method with codeId and userId
     * @param name
     *      The name of the document to find (either username or hash)
     * @return
     *      Always returns null
     */
    @Override
    public ScannedCode getByName(String name) {
        return null;
    }

    /**
     * Gets a ScannedCode by its QRCode and User if it exists
     * @param codeId
     *      The id of the QRCode that was scanned
     * @param userId
     *      The id of the User that scanned the code
     * @return
     *      Returns the ScannedCode if it exists or null
     */
    public ScannedCode getByName(String codeId, String userId) {
        Task<QuerySnapshot> task = collection.whereEqualTo("code", codeId).whereEqualTo("user", userId).get();
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
            return ScannedCode.fromMap(map);
        }
        return null;
    }

    /**
     * Gets all ScannedCodes within the collection
     * @return
     *      Returns a list of all ScannedCodes within the collection
     */
    @Override
    public ArrayList<ScannedCode> getAll() {
        Task<QuerySnapshot> task = collection.get();
        while(!task.isComplete());
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        ArrayList<ScannedCode> result = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            if (doc.getData() != null) {
                Map<String, Object> map = doc.getData();
                map.put("id", doc.getId());
                result.add(ScannedCode.fromMap(map));
            }
        }

        return result;
    }

    /**
     * Updates a ScannedCode within the collection
     * @param data
     *  The new data for the document
     * @return
     *  Returns the ScannedCode within collection after the operation
     */
    @Override
    public ScannedCode update(ScannedCode data) {
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
            return ScannedCode.fromMap(map);
        }
        return null;
    }

    /**
     * Adds a new ScannedCode to the collection
     * @param data
     *  The ScannedCode to add
     * @return
     *  Returns the ScannedCode with id within collection
     */
    @Override
    public ScannedCode add(ScannedCode data) {
        Map<String, Object> sanitizedData = data.toMap();
        Task<DocumentReference> addTask = collection.add(sanitizedData);

        while(!addTask.isComplete());
        Task<DocumentSnapshot> docTask = addTask.getResult().get();
        while(!docTask.isComplete());
        DocumentSnapshot doc = docTask.getResult();
        if (doc != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return ScannedCode.fromMap(map);
        }
        return null;
    }

    /**
     * Deletes a specified ScannedCode within the collection if it exists
     * @param id
     *  The generated id of the ScannedCode to delete
     * @return
     *  Returns the task for deleting the document
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

        Map<String, Object> map = data.getData();
        map.put("id", data.getId());
        ScannedCode code = ScannedCode.fromMap(map);

        // Delete code from user
        String userId = data.get("user", String.class);
        User user = Database.Users.getById(userId);
        user.DeleteQR(code);
        Database.Users.update(user);

        // Delete code from QR Code
        String codeId = data.get("code", String.class);
        QRCode qrCode = Database.QrCodes.getById(codeId);
        qrCode.deleteScannedCode(code);
        Database.QrCodes.update(qrCode);

        return collection.document(id).delete();
    }

    /**
     * Deletes a specified ScannedCode from the collection without removing itself from the associated QRCode
     * @param id
     *      The id of the ScannedCode to delete if it exists
     * @return
     *      Returns the Task for deleting the document
     */
    public Task<Void> deleteFromQR(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be provided");
        }

        Task<DocumentSnapshot> dataTask = collection.document(id).get();
        while (!dataTask.isComplete());
        DocumentSnapshot data = dataTask.getResult();
        if (data.getData() == null) {
            throw new NoSuchElementException(String.format("No such document with id %s", id));
        }

        Map<String, Object> map = data.getData();
        map.put("id", data.getId());
        ScannedCode code = ScannedCode.fromMap(map);

        // Delete code from user
        String userId = data.get("user", String.class);
        User user = Database.Users.getById(userId);
        user.DeleteQR(code);
        Database.Users.update(user);

        return collection.document(id).delete();
    }

    /**
     * Deletes a specified ScannedCode from the collection without removing itself from the associated User
     * @param id
     *      The id of the ScannedCode to delete if it exists
     * @return
     *      Returns the Task for deleting the document
     */
    public Task<Void> deleteFromUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be provided");
        }

        Task<DocumentSnapshot> dataTask = collection.document(id).get();
        while (!dataTask.isComplete());
        DocumentSnapshot data = dataTask.getResult();
        if (data.getData() == null) {
            throw new NoSuchElementException(String.format("No such document with id %s", id));
        }

        Map<String, Object> map = data.getData();
        map.put("id", data.getId());
        ScannedCode code = ScannedCode.fromMap(map);

        // Delete code from QR Code
        String codeId = data.get("code", String.class);
        QRCode qrCode = Database.QrCodes.getById(codeId);
        qrCode.deleteScannedCode(code);
        Database.QrCodes.update(qrCode);

        return collection.document(id).delete();
    }

    /**
     * @deprecated Use the method with codeInfo and userId
     * @param name
     *      The name field of the document to find (either username or hash)
     * @return
     *      Always returns false
     */
    @Override
    public boolean existsName(String name) {
        return false;
    }

    /**
     * Checks if a ScannedCode exists within the collection based off of its QRCode and User
     * @param codeId
     *      The id of the QRCode scanned
     * @param userId
     *      The id of the User that scanned the code
     * @return
     *      Returns whether or not the ScannedCode exists
     */
    public boolean existsName(String codeId, String userId) {
        return this.getByName(codeId, userId) != null;
    }

    /**
     * Checks if a ScannedCode with the specified id exists within the collection
     * @param id
     *      The id of the ScannedCode to search for
     * @return
     *      Returns whether the ScannedCode exists or not
     */
    @Override
    public boolean existsId(String id) {
        return this.getById(id) != null;
    }
}
