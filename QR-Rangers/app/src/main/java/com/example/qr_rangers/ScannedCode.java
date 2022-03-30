package com.example.qr_rangers;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Represents an instance of a User scanning a QRCode
 */
public class ScannedCode extends DbDocument implements Serializable {
    private QRCode code;
    private User user;
    private Location locationScanned;
    private String comment;
    private String photo;

    /**
     * Constructor for ScannedCode
     * @param code
     *      The QRCode that was scanned
     * @param user
     *      The User that scanned the code
     * @param locationScanned
     *      The Location the code was scanned if attached
     * @param comment
     *      The comment the user added to the code if attached
     * @param picture
     *      The picture the user added to the code if attached
     */
    public ScannedCode(QRCode code, User user, @Nullable Location locationScanned, @Nullable String comment, @Nullable String picture) {
        this.code = code;
        this.user = user;
        this.locationScanned = locationScanned;
        this.comment = comment;
        this.photo = picture;
    }

    /**
     * Getter for the QRCode scanned
     * @return
     *      Returns the QRCode this objects is a scan of
     */
    public QRCode getCode() {
        return code;
    }

    /**
     * Setter for the QRCode scanned
     * @param code
     *      The new QRCode scanned
     */
    public void setCode(QRCode code) {
        this.code = code;
    }

    /**
     * Getter for the User that scanned the code
     * @return
     *      Returns the User that scanned the QRCode
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the User that scanned the code
     * @param user
     *      The new User object
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Getter for the Location the code was scanned in
     * @return
     *      The Location attached when the code was scanned or null if the user didn't attach one
     */
    public Location getLocationScanned() {
        return locationScanned;
    }

    /**
     * Setter for the Location the code was scanned in
     * @param locationScanned
     *      The new Location object
     */
    public void setLocationScanned(Location locationScanned) {
        this.locationScanned = locationScanned;
    }

    /**
     * Getter for the comment the user added to the scan
     * @return
     *      The comment attached or null if the user didn't attach one
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setter for the comment added to the scan
     * @param comment
     *      The new comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Getter for the photo attached to the scan
     * @return
     *      Returns the photo attached or null if the user didn't attach one
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Setter for the photo attached to the scan
     * @param photo
     *      The new photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Constructor from a HashMap. Used to convert a document in the database to a ScannedCode
     * @param map
     *      The Hashmap containing the data
     * @return
     *      Returns the ScannedCode constructed from the mapped data
     */
    public static ScannedCode fromMap(Map<String, Object> map) {
        Log.d("SCANNEDCODE", "ScannedCode fromMap");
        HashMap locMap = (HashMap) map.get("locationScanned");
        Location location = null;
        if (locMap != null) {
            location = new Location((double) locMap.get("longitude"), (double) locMap.get("latitude"));
        }
        ScannedCode code =  new ScannedCode(
                Database.QrCodes.getById((String) map.get("code")),
                Database.Users.getById((String) map.get("user")),
                location,
                (String) map.get("comment"),
                (String) map.get("picture"));
        code.setId((String) map.get("id"));
        return code;
    }

    /**
     * Converts this object into a Hashmap for storage
     * @return
     *      Returns a Hashmap with the object's data
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code.getId());
        map.put("user", user.getId());
        map.put("locationScanned", locationScanned);
        map.put("comment", comment);
        map.put("picture", photo);
        return map;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj) {
        if (!(obj instanceof ScannedCode)) {
            throw new IllegalArgumentException();
        }
        return obj != null && ((ScannedCode) obj).getId().compareTo(this.getId()) == 0;
    }
}
