package edu.hm.ba.classic.exception;

/**
 * Thrown to indicate that the book already exists.
 */
public class DuplicateBookException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public DuplicateBookException(String message) {
        super(message);
    }
}
