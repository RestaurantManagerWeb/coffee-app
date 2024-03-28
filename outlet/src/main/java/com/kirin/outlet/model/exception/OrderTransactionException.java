package com.kirin.outlet.model.exception;

import org.springframework.transaction.TransactionException;

public class OrderTransactionException extends TransactionException {

    public OrderTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

}
