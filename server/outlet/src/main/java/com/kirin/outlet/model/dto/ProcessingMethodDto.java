package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * Данные для создания метода обработки
 */
@Getter
public class ProcessingMethodDto {

    /**
     * Конструктор для инициализации данных для создания метода обработки.
     *
     * @param name        уникальное название метода обработки
     * @param description описание (не обязательно)
     */
    public ProcessingMethodDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Уникальное название метода обработки
     */
    @NotBlank
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}(([ -][A-Za-zА-ЯЁа-яё]+)|(( \\()[A-Za-zА-ЯЁа-яё]+\\)))*")
    private String name;

    /**
     * Описание (не обязательно)
     */
    @Size(max = 500)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}" +
            "((([ /-]|(, ))?[\\dA-Za-zА-ЯЁа-яё]+\\.?)|(( \\()[\\dA-Za-zА-ЯЁа-яё]+\\.?\\)))*")
    private String description;

}
