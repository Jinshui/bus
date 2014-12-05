package com.bus.services.dao;

import java.io.Serializable;

public interface IDao<E extends Serializable, K extends Serializable> {

    /**
     * Delete an object.
     * @param o The object
     */
    void delete(E o);

    /**
     * Returns an object based on a primary key value.
     * @param id The id of the object
     * @return The object or null if the object does not exist
     */
    E get(K id);

    /**
     * Persists the object.
     * @param o The object
     */
    void save(E o);

    /**
     * Update the object.
     * @param o The object
     */
    void update(E o);
}