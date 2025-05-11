package repository;
import exception.LibraryException;
import exception.MediaNotFoundException;
import java.util.List;

// Interface for repositories implementing Generics <T = repository type, ID = repository ID type>
public interface Repository<T, ID> {
    
    // Save an entity
    T save(T entity) throws LibraryException;
    
    // Find an entity by ID
    T findById(ID id) throws MediaNotFoundException;
    
    // Find all entities
    List<T> findAll() throws LibraryException;
    
    // Delete an entity by ID
    void delete(ID id) throws MediaNotFoundException;
    
    // Update an entity
    T update(T entity) throws MediaNotFoundException;
}