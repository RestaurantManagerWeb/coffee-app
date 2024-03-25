package com.kirin.outlet.model.exception;

public class IncorrectDataInDatabaseException extends RuntimeException {

    public IncorrectDataInDatabaseException(String message) {
        super(message);
    }

    public IncorrectDataInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
