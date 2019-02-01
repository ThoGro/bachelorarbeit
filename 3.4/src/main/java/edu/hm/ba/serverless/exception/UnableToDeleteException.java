package edu.hm.ba.serverless.exception;

public class UnableToDeleteException extends IllegalStateException {

    public UnableToDeleteException(String message) {
        super(message);
    }
}
