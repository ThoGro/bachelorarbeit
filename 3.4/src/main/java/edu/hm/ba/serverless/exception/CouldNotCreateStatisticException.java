package edu.hm.ba.serverless.exception;

/**
 * Thrown to indicate that the statistic could not be created.
 */
public class CouldNotCreateStatisticException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public CouldNotCreateStatisticException(String message) {
        super(message);
    }
}
