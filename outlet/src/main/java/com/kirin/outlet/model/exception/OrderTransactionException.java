package com.kirin.outlet.model.exception;

import org.springframework.transaction.TransactionException;

/**
 * Ошибка при создании заказа
 */
public class OrderTransactionException extends TransactionException {

    public OrderTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderTransactionException(String msg) {
        super(msg);
    }
}
