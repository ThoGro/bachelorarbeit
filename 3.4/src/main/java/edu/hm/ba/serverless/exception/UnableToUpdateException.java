package edu.hm.ba.serverless.exception;

public class UnableToUpdateException extends IllegalStateException {
    public UnableToUpdateException(String message) {
        super(message);
    }
}
