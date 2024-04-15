package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * Данные для создания позиции на складе
 */
@Getter
public class StockItemDto {

    /**
     * Название позиции
     */
    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}(([ /-]|(, ))?[\\dA-Za-zА-ЯЁа-яё]+\\.?)*")
    private String name;

    /**
     * Уникальный идентификатор единицы измерения
     */
    @Positive
    private int unitMeasureId;

}
