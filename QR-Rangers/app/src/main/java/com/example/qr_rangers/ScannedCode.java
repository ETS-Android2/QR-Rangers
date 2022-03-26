package com.example.qr_rangers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ScannedCode extends DbDocument implements Serializable {
    private QRCode code;
    private User user;
    private Location locationScanned;
    private String comment;
    private String photo;

    public ScannedCode(QRCode code, User user, @Nullable Location locationScanned, @Nullable String comment, @Nullable String picture) {
        this.code = code;
        this.user = user;
        this.locationScanned = locationScanned;
        this.comment = comment;
        this.photo = picture;
    }

    public QRCode getCode() {
        return code;
    }

    public void setCode(QRCode code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocationScanned() {
        return locationScanned;
    }

    public void setLocationScanned(Location locationScanned) {
        this.locationScanned = locationScanned;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static ScannedCode fromMap(Map<String, Object> map) {
        return new ScannedCode(
                Database.QrCodes.getById((String) map.get("code")),
                Database.Users.getById((String) map.get("user")),
                (Location) map.get("locationScanned"),
                (String) map.get("comment"),
                (String) map.get("picture"));
    }

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
        return obj != null && ((ScannedCode) obj).getId() == this.getId();
    }
}
