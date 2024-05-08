package com.kirin.outlet.controller;

import com.kirin.outlet.model.exception.ExceptionBody;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.model.exception.ValidationExceptionBody;
import com.kirin.outlet.model.exception.Violation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * Перехватчик и обработчик ошибок в приложении
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * Элемент не найден
     */
    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody notFoundException(ItemNotFoundException exception) {
        return new ExceptionBody(exception.getMessage(), 404, appName,
                exception.getClass().getSimpleName());
    }

    /**
     * Переданы невалидные данные
     */
    @ResponseBody
    @ExceptionHandler(IncorrectRequestDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionBody incorrectRequestException(
            IncorrectRequestDataException exception
    ) {
        return new ValidationExceptionBody(List.of(exception.getViolation()), 400, appName,
                exception.getClass().getSimpleName());
    }

    /**
     * Ошибка при валидации поступающих данных
     */
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
        return new ValidationExceptionBody(violations, 400, appName,
                exception.getClass().getSimpleName());
    }

    /**
     * Невалидные аргументы метода
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionBody methodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        final List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage())).toList();
        return new ValidationExceptionBody(violations, 400, appName,
                exception.getClass().getSimpleName());
    }

    /**
     * Ошибка при проведении транзакции
     */
    @ResponseBody
    @ExceptionHandler(CannotCreateTransactionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionBody cannotCreateTransactionException(
            CannotCreateTransactionException exception
    ) {
        return new ExceptionBody(exception.getMessage(), 409, appName,
                exception.getClass().getSimpleName());
    }

    /**
     * Передан некорректный json
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionBody notReadableException(HttpMessageNotReadableException exception) {
        final List<Violation> violations =
                List.of(new Violation("notReadableException.exception.httpInputMessage",
                        exception.getMessage()));
        return new ValidationExceptionBody(violations, 400, appName,
                exception.getClass().getSimpleName());
    }

    /**
     * Не найден ресурс (для перехвата исключения с favicon.ico)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public void noResourceException(NoResourceFoundException exception) {
        if (exception.getMessage().equals("No static resource favicon.ico."))
            System.err.println(exception.getMessage());
        else unknownException(exception);
    }

    /**
     * Неотслеживаемые ошибки
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody unknownException(Exception exception) {
        exception.printStackTrace();

        return new ExceptionBody("!!! Неизвестная ошибка. "
                + exception.getMessage(), 500, appName,
                exception.getClass().getSimpleName());
    }
}
