package edu.hm.ba.serverless.exception;

public class CouldNotCreateBookException extends IllegalStateException {
    public CouldNotCreateBookException(String message) {
        super(message);
    }
}
