package com.kirin.outlet.controller;

import com.kirin.outlet.model.exception.ExceptionBody;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.model.exception.OrderTransactionException;
import com.kirin.outlet.model.exception.ValidationExceptionBody;
import com.kirin.outlet.model.exception.Violation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Перехватчик и обработчик ошибок в приложении
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String appName;

    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody notFoundException(ItemNotFoundException exception) {
        return new ExceptionBody(exception.getMessage(), appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(IncorrectRequestDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody incorrectRequestException(
            IncorrectRequestDataException exception
    ) {
        return new ExceptionBody(exception.getMessage(), appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionBody constraintViolationException(
            ConstraintViolationException exception
    ) {
        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )).toList();
        return new ValidationExceptionBody(violations, appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionBody methodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        final List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage())).toList();
        return new ValidationExceptionBody(violations, appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(IncorrectDataInDatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody databaseException(
            IncorrectDataInDatabaseException exception
    ) {
        return new ExceptionBody("Ошибка в базе данных сервиса. "
                + exception.getMessage(), appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(OrderTransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody orderTransactionException(OrderTransactionException exception) {
        // TODO: сделать обработчик для ошибки с созданием заказа
        return new ExceptionBody("Ошибка в базе данных сервиса. "
                + exception.getMessage(), appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody notReadableException(HttpMessageNotReadableException exception) {
        return new ExceptionBody(exception.getMessage(), appName,
                exception.getClass().getSimpleName());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody unknownException(Exception exception) {
        exception.printStackTrace();
        return new ExceptionBody("!!! Неизвестная ошибка. "
                + exception.getMessage(), appName,
                exception.getClass().getSimpleName());
    }
}
