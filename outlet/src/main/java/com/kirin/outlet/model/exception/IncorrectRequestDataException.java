package com.kirin.outlet.model.exception;

/**
 * Передача некорректных данных в запросе
 */
public class IncorrectRequestDataException extends RuntimeException {

    public IncorrectRequestDataException(String message) {
        super(message);
    }

    public IncorrectRequestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
