package com.kirin.outlet.model.exception;

import jakarta.validation.constraints.NotEmpty;
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
     * @param code       код ответа
     * @param appName    имя приложения
     * @param excName    название исключения
     */
    public ValidationExceptionBody(@NotEmpty List<Violation> violations, int code,
                                   String appName, String excName) {
        this.violations = violations;
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
     * Список полей, не прошедших валидацию, с сообщениями об ошибке
     */
    private final List<Violation> violations;

    /**
     * Дата создания (текущие дата и время)
     */
    private final LocalDateTime timestamp;

}
