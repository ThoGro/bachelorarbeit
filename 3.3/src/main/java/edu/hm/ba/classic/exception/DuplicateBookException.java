package edu.hm.ba.classic.exception;

public class DuplicateBookException extends IllegalStateException {
    public DuplicateBookException(String message) {
        super(message);
    }
}
