package com.kirin.outlet.model.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация об ошибке при проверке данных
 */
@Getter
public class ValidationExceptionBody {

    /**
     * Конструктор для задания информации об ошибке. Фиксируется дата и время создания.
     *
     * @param violations список полей, не прошедших валидацию
     * @param appName    имя приложения
     * @param excName    название исключения
     */
    public ValidationExceptionBody(List<Violation> violations, String appName, String excName) {
        if (violations != null) this.violations = violations;
        else this.violations = new ArrayList<>();
        this.appName = appName;
        this.excName = excName;
        timestamp = LocalDateTime.now();
    }

    /**
     * Имя сервиса (приложения), в котором возникла ошибка
     */
    private final String appName;

    /**
     * Название исключения
     */
    private final String excName;

    /**
     * Список полей, не прошедших валидацию, с сообщениями об ошибке
     */
    private final List<Violation> violations;

    /**
     * Дата создания (текущие дата и время)
     */
    private final LocalDateTime timestamp;

}
