package edu.hm.ba.serverless.exception;

/**
 * Thrown to indicate that the table does not exist.
 */
public class TableDoesNotExistException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public TableDoesNotExistException(String message) {
        super(message);
    }
}
