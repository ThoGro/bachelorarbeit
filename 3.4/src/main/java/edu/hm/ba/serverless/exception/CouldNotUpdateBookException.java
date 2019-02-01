package edu.hm.ba.serverless.exception;

public class CouldNotUpdateBookException extends IllegalStateException {
    public CouldNotUpdateBookException(String message) {
        super(message);
    }
}
