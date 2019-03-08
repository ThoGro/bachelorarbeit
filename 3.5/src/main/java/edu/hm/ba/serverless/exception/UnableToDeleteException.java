package edu.hm.ba.serverless.exception;

/**
 * Thrown to indicate that the deletion could not be performed.
 */
public class UnableToDeleteException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public UnableToDeleteException(String message) {
        super(message);
    }
}
