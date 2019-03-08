package edu.hm.ba.classic.exception;

/**
 * Thrown to indicate that the book does not exist.
 */
public class BookDoesNotExistException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public BookDoesNotExistException(String message) {
        super(message);
    }
}
