package com.kirin.outlet.model.exception;

import lombok.Getter;

/**
 * Передача некорректных данных в запросе
 */
@Getter
public class IncorrectRequestDataException extends RuntimeException {

    private final Violation violation;

    public IncorrectRequestDataException(String fieldName, String message) {
        super(message);
        violation = new Violation(fieldName, message);
    }

    public IncorrectRequestDataException(String fieldName, String message, Throwable cause) {
        super(message, cause);
        violation = new Violation(fieldName, message);
    }
}
