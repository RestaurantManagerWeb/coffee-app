package com.kirin.outlet.model.exception;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Информация об ошибке
 */
@Getter
public class ExceptionBody {

    /**
     * Конструктор для задания информации об ошибке. Фиксируется дата и время создания.
     *
     * @param message сообщение об ошибке
     * @param code    код состояния ответа HTTP
     * @param appName имя приложения
     * @param excName название исключения
     */
    public ExceptionBody(String message, int code, String appName, String excName) {
        this.message = message;
        this.code = code;
        this.appName = appName;
        this.excName = excName;
        timestamp = LocalDateTime.now();
    }

    /**
     * Имя сервиса (приложения), в котором возникла ошибка
     */
    private final String appName;

    /**
     * Код состояния ответа HTTP
     */
    private final int code;

    /**
     * Название исключения
     */
    private final String excName;

    /**
     * Сообщение об ошибке
     */
    private final String message;

    /**
     * Дата создания (текущие дата и время)
     */
    private final LocalDateTime timestamp;

}
