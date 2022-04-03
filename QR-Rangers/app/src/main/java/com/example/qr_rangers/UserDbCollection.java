package com.example.qr_rangers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserDbCollection implements IDbCollection<User> {
    private CollectionReference collection;

    public UserDbCollection(CollectionReference collection) {
        this.collection = collection;
    }

    /**
     * Gets a User by its generated id if it exists
     * @param id
     *      The generated id of the User
     * @return
     *      Returns the User if it exists or null
     */
    @Override
    public User getById(String id) {
        if (id == null) {
            return null;
        }
        Task<DocumentSnapshot> documentSnapshotTask = collection.document(id).get();
        while (!documentSnapshotTask.isComplete());
        DocumentSnapshot doc = documentSnapshotTask.getResult();
        if (doc.getData() != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return User.fromMap(map);
        }
        return null;
    }

    /**
     * Gets a User by its username if it exists
     * @param name
     *      The username of the User to find
     * @return
     *      Returns the User if it exists or null
     */
    @Override
    public User getByName(String name) {
        ArrayList<User> result = new ArrayList<>();
        Task<QuerySnapshot> task = collection.whereEqualTo("username", name).get();
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
            return User.fromMap(map);
        }
        return null;
    }

    /**
     * Gets all Users within the collection
     * @return
     *      Returns a list of all documents within the collection
     */
    @Override
    public ArrayList<User> getAll() {
        Task<QuerySnapshot> task = collection.get();
        while(!task.isComplete());
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        ArrayList<User> result = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            if (doc.getData() != null) {
                Map<String, Object> map = doc.getData();
                map.put("id", doc.getId());
                result.add(User.fromMap(map));
            }
        }

        return result;
    }

    /**
     * Gets all the top 10 user scores based on a provided score type
     * @param rankType
     *  Communicates how to rank the users
     * @return
     *  Returns a list of the 10 users with the highest of the requested score
     */
    public ArrayList<User> displayLeaderboard(String rankType) {
        Task<QuerySnapshot> task = collection.orderBy(rankType, Query.Direction.DESCENDING).limit(10).get();
        while(!task.isComplete());
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        ArrayList<User> result = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            if (doc.getData() != null) {
                Map<String, Object> map = doc.getData();
                map.put("id", doc.getId());
                result.add(User.fromMap(map));
            }
        }

        return result;
    }

    /**
     * Updates the ranks of a specific user given a specific score to rank by
     * @param rankType
     *  Communicates how to rank the users
     * @param currentUser
     *  User that needs its ranks updated
     */
    public int updateRanks(String rankType, User currentUser) {
        Task<QuerySnapshot> task = collection.orderBy(rankType, Query.Direction.DESCENDING).get();
        while(!task.isComplete());
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        int i = 1;
        Boolean reached = false;
        for (DocumentSnapshot doc : docs) {
            if (doc.getData() != null) {
                Map<String, Object> map = doc.getData();
                map.put("id", doc.getId());
                User user = User.fromMap(map);
                if (currentUser.equals(user)) {
                    reached = true;
                }
                if (reached) {
                    switch (rankType) {
                        case "scoreSum":
                            if (i != user.getUserRanks().getTotalScoreRank()) {
                                user.getUserRanks().setTotalScoreRank(i);
                                reached = false;
                            }
                            break;
                        case "qrnum":
                            if (i != user.getUserRanks().getQRScannedRank()) {
                                user.getUserRanks().setQRScannedRank(i);
                                reached = false;
                            }
                            break;
                        case "scoreMax":
                            if (i != user.getUserRanks().getBestQRRank()) {
                                user.getUserRanks().setBestQRRank(i);
                                reached = false;
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Not a valid rank to update!");
                    }
                    if (!reached) {
                        Database.Users.update(user);
                    }
                    return i;
                }
                i += 1;
            }
        }
        return -1;
    }

    /**
     * Updates a User within the collection
     * @param data
     *  The new data for the User
     * @return
     *  Returns the document within collection after the operation
     */
    @Override
    public User update(User data) {
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
            return User.fromMap(map);
        }
        return null;
    }

    /**
     * Adds a new User to the collection
     * @param data
     *  The User to add
     * @return
     *  Returns the document with generated id within collection
     */
    @Override
    public User add(User data) {
        Map<String, Object> sanitizedData = data.toMap();
        Task<DocumentReference> addTask = collection.add(sanitizedData);

        while(!addTask.isComplete());
        Task<DocumentSnapshot> docTask = addTask.getResult().get();
        while(!docTask.isComplete());
        DocumentSnapshot doc = docTask.getResult();
        if (doc != null) {
            Map<String, Object> map = doc.getData();
            map.put("id", doc.getId());
            return User.fromMap(map);
        }
        return null;
    }

    /**
     * Deletes a specified User within the collection if it exists
     * @param id
     *  The id of the User to delete
     * @return
     *  Returns the task for deleting the User
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

        // Delete ScannedCodes
        ArrayList<String> scannedCodeIds = (ArrayList<String>) data.get("qrList");
        if (scannedCodeIds != null) {
            for (String codeId : scannedCodeIds) {
                Database.ScannedCodes.deleteFromUser(codeId);
            }
        }
        return collection.document(id).delete();
    }

    /**
     * Checks if a User with the specified username exists within the collection
     * @param name
     *      The name username of the User to search for
     * @return
     *      Returns whether the User exists or not
     */
    @Override
    public boolean existsName(String name) {
        return this.getByName(name) != null;
    }

    /**
     * Checks if a User with the specified id exists within the collection
     * @param id
     *      The id of the User to search for
     * @return
     *      Returns whether the User exists or not
     */
    @Override
    public boolean existsId(String id) {
        return this.getById(id) != null;
    }

    /**
     * A function to check for usernames that begin with what was queried
     *
     * @param name
     *      The username fragment that is being searched
     *
     * @return
     *      Returns an array list containing all users that begin with the username fragment
     */
    public ArrayList<User> searchSuggestions(String name) {
        ArrayList<String> adminIds = Database.Admins.getAll();

        ArrayList<User> results = new ArrayList<>();
        Task<QuerySnapshot> task = collection.whereGreaterThanOrEqualTo("username", name).whereLessThanOrEqualTo("username", name+"\uf8ff").get();
        while(!task.isComplete());
        QuerySnapshot snap = task.getResult();
        for(DocumentSnapshot doc: snap.getDocuments()){
            if(doc.getData() != null){
                User user = User.fromMap(doc.getData());
                user.setId(doc.getId());
                if (!adminIds.contains(user.getId())) {
                    results.add(user);
                }
            }
        }
        Log.i("TAG", Integer.toString(snap.size()));
        return results;
    }
}
