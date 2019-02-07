package edu.hm.ba.classic.exception;

public class CouldNotLendBookException extends IllegalStateException {
    public CouldNotLendBookException(String message) {
        super(message);
    }
}
