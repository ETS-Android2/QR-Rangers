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

public class ScannedCodeDbCollection implements IDbCollection<ScannedCode> {
    private CollectionReference collection;

    public ScannedCodeDbCollection(CollectionReference collection) {
        this.collection = collection;
    }

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

    public ScannedCode getByName(String codeId, String userId) {
        ArrayList<User> result = new ArrayList<>();
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
        return null;    }

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

    public boolean existsName(String codeId, String userId) {
        return this.getByName(codeId, userId) != null;
    }

    @Override
    public boolean existsId(String id) {
        return this.getById(id) != null;
    }
}
