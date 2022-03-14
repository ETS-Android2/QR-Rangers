package com.example.qr_rangers;

import java.util.ArrayList;

/**
 * An object to keep track of a DbCollection in order to locate users for Search
 *
 * @author Jawdat
 * @version 1.0.2
 */
public class Search {
    private DbCollection db;

    Search(){
        db = new DbCollection("users");
    }

    /**
     * A function to return a User object corresponding with a username
     *
     * @param username
     *      The username that we want to find
     * @return
     *      Returns an ArrayList containing the User object corresponding with the username
     */
    public ArrayList<User> FindUser(String username){
        return db.FindUser(username);
    }

    /**
     * A function to check whether a username exists in the database
     *
     * @param username
     *      The username that we want to check for
     * @return
     *      A boolean, true if that username is being used, false otherwise
     */
    public boolean CheckUser(String username){
        return db.CheckUser(username);
    }

    /**
     * A function to check for usernames that begin with what was queried
     *
     * @param username
     *      The username fragment that is being searched
     *
     * @return
     *      Returns an array list containing all users that begin with the username fragment
     */
    public ArrayList<User> searchSuggestions(String username){
        return db.searchSuggestions(username);
    }
}