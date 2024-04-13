package com.kirin.outlet.model.exception;

/**
 * Информация о поле, не прошедшем валидацию
 *
 * @param fieldName имя поля
 * @param message   сообщение об ошибке
 */
public record Violation(String fieldName, String message) {
}
