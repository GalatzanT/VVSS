package drinkshop.service.exception;

/**
 * Exception thrown when a persistence operation fails.
 * Unifies error handling across the repository layer.
 */
public class PersistenceException extends RuntimeException {
    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
