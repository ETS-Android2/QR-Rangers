package com.example.qr_rangers;

import android.util.Log;

import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that represents the user, containing all their QR Codes and scores
 *
 * @author Jawdat and Ryan
 * @version 1.7
 */
public class User extends DbDocument implements Serializable {
    private String username;
    private String email;
    private String phoneNumber;
    private ArrayList<ScannedCode> QRList;
    private ArrayList<String> QRIds;
    protected Rankings userRanks;
    private int totalScore;
    private int maxScore;
    private int minScore;

    /**
     * Constructs a user object
     *
     * @param username The unique username of the user
     * @param email The email of the user
     * @param phoneNumber The phone number of the user
     *
     */
    public User(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.QRList = new ArrayList<>();
        this.QRIds = new ArrayList<>();
        this.userRanks = new Rankings();
        this.maxScore = 0;
        this.minScore = -1;
        this.totalScore = 0;
    }

    /**
     * Constructs a user object. Uses no arguments for serialization
     */
    public User(){
        this.username = "";
        this.email = "";
        this.phoneNumber = "";
        QRList = new ArrayList<>();
        QRIds = new ArrayList<>();
        this.userRanks = new Rankings();
        this.maxScore = 0;
        this.minScore = -1;
        this.totalScore = 0;
    }

    /**
     * Gets the username of the user object
     *
     * @return
     *      A String that represents the username of the user object
     */
    public String getUsername(){
        return username;
    }

    /**
     * Sets the username of the user object
     *
     * @param username
     *      The new username to replace the original one
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email of the user object
     *
     * @return
     *      A String that represents the email of the user object
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user object
     *
     * @param email
     *      The new email to replace the original one
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the user object
     *
     * @return
     *      A String that represents the phone number of the user object
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user object
     *
     * @param phoneNumber
     *      The new phone number to replace the original one
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the ScannedCode list of the user object
     *
     * @return
     *      An arraylist that represents the list of ScannedCodes from the user object
     */
    public ArrayList<ScannedCode> getQRList() {
        this.loadQRList();
        return QRList;
    }

    /**
     * Get total score of User
     * @return Total score int of user
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Set total score of User
     * @param totalScore int to set total score to
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Get max score of user
     * @return maxScore int of user
     */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * Set max score of User
     * @param maxScore int to set max score to
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * Get min score of User
     * @return min score int of user
     */
    public int getMinScore() {
        return minScore;
    }

    /**
     * Set min score of User
     * @param minScore int to set min score to
     */
    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    /**
     * Gets the list of ids for ScannedCodes attached to the User
     * @return
     *      Returns the list of ids
     */
    public ArrayList<String> getQRIds() {
        return this.QRIds;
    }

    /**
     * Sets the list of ScannedCode ids for the User
     * @param QRIds
     *      List of ScannedCode ids
     */
    public void setQRIds(ArrayList<String> QRIds) {
        this.QRIds = QRIds;
    }

    /**
     * Adds a QR Code to the user's list
     *
     * @param code The QRCode that is to be added
     * @return Returns true if the add works, returns false if the QR Code already exists in the list, avoids duplicates
     */
    public boolean AddQR(ScannedCode code) {
        if (QRIds.contains(code.getId())) {
            return false;
        }
        ScannedCode dbCode;
        if (!Database.ScannedCodes.existsName(code.getCode().getId(), code.getUser().getId())) {
            dbCode = Database.ScannedCodes.add(code);
            QRCode qrCode = Database.QrCodes.getById(code.getCode().getId());
            qrCode.addScannedCode(dbCode);
            Database.QrCodes.update(qrCode);
        } else {
            dbCode = Database.ScannedCodes.getByName(code.getCode().getId(), code.getUser().getId());
        }

        QRList.add(dbCode);
        QRIds.add(dbCode.getId());
        int currentScore = code.getCode().getScore();
        setTotalScore(getTotalScore() + currentScore);
        if (currentScore > getMaxScore()) {
            setMaxScore(currentScore);
        }
        if (getMinScore() == -1 || currentScore < getMinScore()) {
            setMinScore(currentScore);
        }
        return true;
    }

    /**
     * Removes a particular ScannedCode from the list
     *
     * @param code The ScannedCode we want to delete
     * @throws IllegalArgumentException If the ScannedCode does not exist in the list
     */
    public void DeleteQR(ScannedCode code) {
        if (QRIds.contains(code.getId())) {
            if (QRList.size() > 0) {
                QRList.remove(code);
            }
            QRIds.remove(code.getId());
            int currentScore = code.getCode().getScore();
            setTotalScore(getTotalScore() - currentScore);
            if (currentScore == getMaxScore()) {
                setMaxScore(getScoreMax());
            }
            if (currentScore == getMinScore()) {
                setMinScore(getScoreMin());
            }
            Database.ScannedCodes.deleteFromUser(code.getId());
        } else { // Not sure why this would ever happen but
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if the User has a ScannedCode
     * @param code
     *      The ScannedCode to check
     * @return
     *      Returns whether or not the User has the ScannedCode
     */
    public boolean HasQR(ScannedCode code) {
        if (code.getUser() != this) {
            return false;
        }
        return Database.ScannedCodes.existsName(code.getCode().getId(), code.getUser().getId());
    }

    /**
     * Gets the maximum score of all QR Codes in the QR List
     *
     * @return
     *      Returns the maximum of all of the scores in the QR List
     */
    public int getScoreMax(){
        this.loadQRList();
        int score = 0;
        for (ScannedCode code: QRList) {
            if(code.getCode().getScore() > score){
                score = code.getCode().getScore();
            }
        }
        return score;
    }

    /**
     * Gets the minimum score of all QR Codes in the QR List
     *
     * @return
     *      Returns the minimum of all of the scores in the QR List
     */
    public int getScoreMin(){
        this.loadQRList();
        if(getQRNum() > 0) {
            int score = Integer.MAX_VALUE;
            for (ScannedCode code : QRList) {
                if (code.getCode().getScore() < score) {
                    score = code.getCode().getScore();
                }
            }
            return score;
        }
        else{
            return 0; // idk what we want to return in these cases
        }
    }

    /**
     * Gets the number QR Codes in the QR List
     *
     * @return
     *      Returns the number of codes in the QR List
     */
    public int getQRNum(){
        return QRIds.size();
    }

    /**
     * Gets the Rankings object of the User
     *
     * @return
     *      Returns the user's Rankings object
     */
    public Rankings getUserRanks() {
        return userRanks;
    }

    /**
     * Sets the user's Rankings object
     * @param userRanks
     * Rankings object to set to user
     */
    public void setUserRanks(Rankings userRanks) {
        this.userRanks = userRanks;
    }

    /**
     * Loads ScannedCodes from ids
     */
    public void loadQRList() {
        if (this.QRList.size() == this.QRIds.size()) {
            return;
        }
        for (String id : this.QRIds) {
            ScannedCode code = Database.ScannedCodes.getById(id);
            if (code != null) {
                this.QRList.add(code);
            }
        }
    }

    /**
     * Creates a new User object from a map representation
     * @param map
     * The map containing the values for the object
     * @return
     *      Returns the created User object
     */
    public static User fromMap(Map<String, Object> map) {
        Log.d("USER", "User fromMap");
        User user = new User((String) map.get("username"), (String) map.get("email"), (String) map.get("phoneNumber"));
        user.setId((String) map.get("id"));
        user.setQRIds((ArrayList<String>) map.get("QRList"));
        user.setMaxScore(Math.toIntExact((Long) map.get("scoreMax")));
        user.setMinScore(Math.toIntExact((Long) map.get("scoreMin")));
        user.setTotalScore(Math.toIntExact((Long) map.get("scoreSum")));
        user.userRanks = Rankings.fromMap((Map) map.get("rankings"));
        if (user.getQRIds() == null) {
            user.setQRIds(new ArrayList<>());
        }
        return user;
    }

    /**
     * Converts the User into a map representation for inserting into the Firestore
     * @return
     *      Returns the map representation of the User
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("QRList", this.QRIds);
        map.put("qrnum", this.getQRNum());
        map.put("scoreMax", this.getMaxScore());
        map.put("scoreMin", this.getMinScore());
        map.put("scoreSum", this.getTotalScore());
        map.put("username", this.getUsername());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("rankings", this.userRanks.toMap());
        return map;
    }

    /**
     * An equals override function to set equivalence to being the same QR Code
     *
     * @param user
     *      Another QRCode object to compare to
     *
     * @return
     *      Returns true if the QRCode objects both contain the same QRCode, false otherwise
     *
     * @throws IllegalArgumentException
     *      If we are not comparing to a QRCode, this is a problem
     */
    @Override
    public boolean equals(Object user) {
        if(!(user instanceof User)){
            throw new IllegalArgumentException();
        }
        return username.equals(((User)user).getUsername());
    }
}

