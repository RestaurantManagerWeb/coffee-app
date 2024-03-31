package com.kirin.outlet.model.exception;

/**
 * Наличие некорректных данных или связей в базе данных сервиса
 */
public class IncorrectDataInDatabaseException extends RuntimeException {

    public IncorrectDataInDatabaseException(String message) {
        super(message);
    }

    public IncorrectDataInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
