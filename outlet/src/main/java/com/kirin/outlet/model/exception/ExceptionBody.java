package com.kirin.outlet.model.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Информация об ошибке
 */
@Getter
@ToString
@EqualsAndHashCode
public class ExceptionBody {

    /**
     * Имя сервиса (приложения), в котором возникла ошибка
     */
    private final String appName;

    /**
     * Сообщение об ошибке
     */
    private final String message;

    /**
     * Дата создания (текущие дата и время)
     */
    private final LocalDateTime dateTime;

    /**
     * Конструктор для задания информации об ошибке. Фиксируется дата и время создания.
     * @param message сообщение об ошибке
     * @param appName имя приложения
     */
    public ExceptionBody(String message, String appName) {
        this.message = message;
        this.appName = appName;
        dateTime = LocalDateTime.now();
    }

}
