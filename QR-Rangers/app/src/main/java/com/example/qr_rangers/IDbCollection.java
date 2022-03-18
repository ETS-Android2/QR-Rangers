package com.example.qr_rangers;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public interface IDbCollection<T extends DbDocument> {
    /**
     * Gets a document by its generated id if it exists
     * @param id
     *      The generated id of the document
     * @return
     *      Returns the document if it exists or null
     */
    public T getById(String id);

    /**
     * Gets a document by its name field if it exists
     * @param name
     *      The name of the document to find (either username or hash)
     * @return
     *      Returns the document if it exists or null
     */
    public T getByName(String name);

    /**
     * Gets all documents within the collection
     * @return
     *      Returns a list of all documents within the collection
     */
    public ArrayList<T> getAll();

    /**
     * Updates a document within the collection
     * @param data
     *  The new data for the document
     * @return
     *  Returns the document within collection after the operation
     */
    public T update(T data);

    /**
     * Adds a new document to the collection
     * @param data
     *  The document to add
     * @return
     *  Returns the document with id within collection
     */
    public T add (T data);

    /**
     * Deletes a specified document within the collection if it exists
     * @param id
     *  The generated id of the document to delete
     * @return
     *  Returns the task for deleting the document
     */
    public Task<Void> delete(String id);

    /**
     * Checks if a document with the specified name exists within the collection
     * @param name
     *      The name field of the document to find (either username or hash)
     * @return
     *      Returns whether the document exists or not
     */
    public boolean existsName(String name);

    /**
     * Checks if a document with the specified id exists within the collection
     * @param id
     *      The id of the document to search for
     * @return
     *      Returns whether the document exists or not
     */
    public boolean existsId(String id);

}
