package com.hospital.database.dao;

import java.util.List;

/**
 * Generic Data Access Object (DAO) interface to enforce Abstraction.
 * Defines standard CRUD operations for any domain model.
 * 
 * @param <T> The model class type.
 */
public interface Dao<T> {
    /**
     * Saves an entity object to the database.
     * @param t The object to save.
     * @return true if save operation was successful, false otherwise.
     */
    boolean save(T t);

    /**
     * Retrieves all entity records from the database.
     * @return List of entities.
     */
    List<T> getAll();
}
