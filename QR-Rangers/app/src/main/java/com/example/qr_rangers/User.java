package com.example.qr_rangers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends DbDocument implements Serializable {
    private String username;
    private String email;
    private String phoneNumber;
    private ArrayList<QRCode> QRList;

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
        QRList = new ArrayList<QRCode>();
    }

    /**
     * Constructs a user object. Uses no arguments for serialization
     */
    public User(){
        this.username = "";
        this.email = "";
        this.phoneNumber = "";
        QRList = new ArrayList<QRCode>();
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
     * Gets the QR code list of the user object
     *
     * @return
     *      An arraylist that represents the list of QR codes from the user object
     */
    public ArrayList<QRCode> getQRList() {
        return QRList;
    }

    /**
     * Sets the QR code list of the user object
     *
     * @param QRList
     *      The new QR code list to replace the original one
     */
    public void setQRList(ArrayList<QRCode> QRList) {
        this.QRList = QRList;
    }

    /**
     * Adds a QR Code to the user's list
     *
     * @param code The QRCode that is to be added
     * @return Returns true if the add works, returns false if the QR Code already exists in the list, avoids duplicates
     */
    public boolean AddQR(QRCode code) {
        if (QRList.contains(code)) {
            return false;
        }
        QRList.add(code);
        return true;
    }

    /**
     * Removes a particular QRCode from the list
     *
     * @param code The QRCode we want to delete
     * @throws IllegalArgumentException If the QRCode does not exist in the list
     */
    public void DeleteQR(QRCode code) {
        if (QRList.contains(code)) {
            QRList.remove(code);
        } else { // Not sure why this would ever happen but
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the sum of the scores of all QR Codes in the QR List
     *
     * @return
     *      Returns the sum of all of the scores in the QR List
     */
    public int getScoreSum() {
        int score = 0;
        for (QRCode code : QRList) {
            score += code.getScore();
        }
        return score;
    }

    /**
     * Gets the maximum score of all QR Codes in the QR List
     *
     * @return
     *      Returns the maximum of all of the scores in the QR List
     */
    public /*QRCode*/ int getScoreMax(){
        /*QRCode max_code = QRList.get(0); // maybe we could return the QR Code itself?
        for (QRCode code: QRList) {
            if(code.getScore() > max_code.getScore()){
                max_code = code;
            }
        }
        return max_code;*/
        int score = 0;
        for (QRCode code: QRList) {
            if(code.getScore() > score){
                score = code.getScore();
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
    public /*QRCode*/ int getScoreMin(){
        /*QRCode min_code = QRList.get(0); // maybe we could return the QR Code itself?
        for (QRCode code: QRList) {
            if(code.getScore() < min_code.getScore()){
                min_code = code;
            }
        }
        return min_code;*/
        if(getQRNum() > 0) {
            int score = Integer.MAX_VALUE;
            for (QRCode code : QRList) {
                if (code.getScore() < score) {
                    score = code.getScore();
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
        return QRList.size();
    }

    @Override
    public DbDocument fromMap(Map<String, Object> map) {
        User user = new User((String) map.get("username"), (String) map.get("email"), (String) map.get("phoneNumber"));
        user.setId((String) map.get("id"));
        QRCode helper = new QRCode();
        ArrayList<HashMap> qrList = (ArrayList<HashMap>) map.get("QRList");
        for (int i = 0; i < qrList.size(); i++) user.AddQR((QRCode) helper.fromMap(qrList.get(i)));
        return user;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<HashMap> qrList = new ArrayList<>();
        for (int i = 0; i < this.QRList.size(); i++) {
            qrList.add((HashMap) this.QRList.get(i).toMap());
        }
        map.put("QRList", qrList);
        map.put("qrnum", this.getQRNum());
        map.put("scoreMax", this.getScoreMax());
        map.put("scoreMin", this.getScoreMin());
        map.put("scoreSum", this.getScoreSum());
        map.put("username", this.getUsername());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
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

