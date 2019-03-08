package edu.hm.ba.classic.exception;

/**
 * Thrown to indicate that the book could not be created.
 */
public class CouldNotCreateBookException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public CouldNotCreateBookException(String message) {
        super(message);
    }
}
