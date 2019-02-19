package edu.hm.ba.classic.exception;

/**
 * Thrown to indicate that the book could not be lent.
 */
public class CouldNotLendBookException extends IllegalStateException {

    /**
     * Constructs the Exception with the specified detail message.
     * @param message detail message
     */
    public CouldNotLendBookException(String message) {
        super(message);
    }
}
