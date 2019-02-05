package edu.hm.ba.classic.exception;

public class BookDoesNotExistException extends IllegalStateException {
    public BookDoesNotExistException(String message) {
        super(message);
    }
}
