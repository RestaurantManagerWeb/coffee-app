package com.kirin.outlet.controller;

import com.kirin.outlet.model.exception.ExceptionBody;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.model.exception.OrderTransactionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Перехватчик и обработчик ошибок в приложении
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String appName;

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ExceptionBody> notFoundException(ItemNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionBody(exception.getMessage(), appName));
    }

    @ExceptionHandler(IncorrectRequestDataException.class)
    public ResponseEntity<ExceptionBody> incorrectRequestException(
            IncorrectRequestDataException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionBody(exception.getMessage(), appName));
    }

    @ExceptionHandler(IncorrectDataInDatabaseException.class)
    public ResponseEntity<ExceptionBody> databaseException(
            IncorrectDataInDatabaseException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody("Ошибка в базе данных сервиса. "
                        + exception.getMessage(), appName));
    }

    @ExceptionHandler(OrderTransactionException.class)
    public ResponseEntity<ExceptionBody> orderTransactionException(
            OrderTransactionException exception
    ) {
        // TODO: сделать обработчик для ошибки с созданием заказа
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody("Ошибка в базе данных сервиса. "
                        + exception.getMessage(), appName));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> unknownException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody("!!! Неизвестная ошибка. "
                        + exception.getMessage(), appName));
    }
}
