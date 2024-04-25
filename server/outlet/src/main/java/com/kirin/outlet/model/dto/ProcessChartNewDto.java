package com.kirin.outlet.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * Данные для создания техкарты
 */
@Getter
public class ProcessChartNewDto {

    /**
     * Конструктор для инициализации данных для создания техкарты.
     *
     * @param yield       выход в граммах
     * @param portion     количество порций, по умолчанию 1
     * @param description описание приготовления, может быть null
     */
    public ProcessChartNewDto(int yield, Integer portion, String description) {
        this.yield = yield;
        this.portion = portion;
        this.description = description;
    }

    /**
     * Описание приготовления, может быть null
     */
    @Nullable
    @Size(max = 3000)
    @Pattern(regexp = "^[\\w\\sА-ЯЁа-яё\\-,%/№^&*!?#+=\\.:;'\"()]{3,}")
    private String description;

    /**
     * Выход в граммах
     */
    @Positive
    private int yield;

    /**
     * На сколько порций рассчитана техкарта, по умолчанию 1
     */
    @Nullable
    @Min(1)
    private Integer portion;

}
