package com.example.qr_rangers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class DbDocument implements Serializable {
    private String id;

    /**
     * Getter for the document's auto-generated id
     * @return
     *      Returns the auto-generated id for the document within the Firestore
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the document's id, generally used to update a document with its auto-generated id
     * @param id
     *      The new id value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Creates a new object from a key-value map representation
     * @param map
     *      The map containing the values for the object
     * @return
     *      Returns the created DbDocument object
     */
    public abstract DbDocument fromMap(Map<String, Object> map);

    /**
     * Converts the object to a key-value map to insert into the Firestore
     * @return
     *      Returns a map representation of the object
     */
    public abstract Map<String, Object> toMap();
}
