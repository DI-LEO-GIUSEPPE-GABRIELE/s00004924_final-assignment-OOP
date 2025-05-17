package repository;

import exception.LibraryException;
import exception.MediaNotFoundException;
import java.util.List;

// Interface for repositories implementing Generics <T = repository type, ID = repository ID type>
public interface Repository<T, ID> {

    /**
     * Save an entity
     * 
     * @param entity - The entity to save
     * @return The saved entity
     * @throws LibraryException - If there is an error during the save operation
     */
    T save(T entity) throws LibraryException;

    /**
     * Find an entity by ID
     * 
     * @param id - The ID of the entity to find
     * @return The found entity
     * @throws MediaNotFoundException - If the entity with the specified ID is not
     *                                found
     */
    T findById(ID id) throws MediaNotFoundException;

    /**
     * Find all entities
     * 
     * @return List of all entities
     * @throws LibraryException - If there is an error retrieving the entities list
     */
    List<T> findAll() throws LibraryException;

    /**
     * Delete an entity by ID
     * 
     * @param id - The ID of the entity to delete
     * @throws MediaNotFoundException - If the entity with the specified ID is not
     *                                found
     */
    void delete(ID id) throws MediaNotFoundException;

    /**
     * Update an entity
     * 
     * @param entity - The entity to update
     * @return The updated entity
     * @throws MediaNotFoundException - If the entity to update is not found
     */
    T update(T entity) throws MediaNotFoundException;
}