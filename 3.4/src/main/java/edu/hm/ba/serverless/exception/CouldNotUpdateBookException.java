package edu.hm.ba.serverless.exception;

/**
 * Thrown to indicate that the book could not be updated.
 */
public class CouldNotUpdateBookException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public CouldNotUpdateBookException(String message) {
        super(message);
    }
}
