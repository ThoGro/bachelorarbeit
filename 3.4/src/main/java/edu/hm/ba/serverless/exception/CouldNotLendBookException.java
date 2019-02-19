package edu.hm.ba.serverless.exception;

public class CouldNotLendBookException extends IllegalStateException {
    public CouldNotLendBookException(String message) {
        super(message);
    }
}
