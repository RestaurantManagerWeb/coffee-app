package com.kirin.outlet.model.exception;

/**
 * Не найдены запрашиваемые данные
 */
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
