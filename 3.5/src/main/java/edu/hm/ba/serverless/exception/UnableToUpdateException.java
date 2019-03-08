package edu.hm.ba.serverless.exception;

/**
 * Thrown to indicate that the update could not be performed.
 */
public class UnableToUpdateException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public UnableToUpdateException(String message) {
        super(message);
    }
}
