package com.kirin.outlet.controller;

import com.kirin.outlet.model.exception.ExceptionBody;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.model.exception.OrderTransactionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String appName;

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ExceptionBody> notFoundException(ItemNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionBody(exception.getMessage()));
    }

    @ExceptionHandler(IncorrectDataInDatabaseException.class)
    public ResponseEntity<ExceptionBody> notFoundException(IncorrectDataInDatabaseException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody(
                        "Ошибка в базе данных сервиса " + appName + ". "
                        + exception.getMessage()));
    }

    @ExceptionHandler(OrderTransactionException.class)
    public ResponseEntity<ExceptionBody> notFoundException(OrderTransactionException exception) {
        // TODO: сделать обработчик для ошибки с созданием заказа
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody(
                        "Ошибка в базе данных сервиса " + appName + ". "
                        + exception.getMessage()));
    }
}
