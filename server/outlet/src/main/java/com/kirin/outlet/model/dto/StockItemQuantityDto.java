package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

/**
 * Данные об ID позиции на складе и ее количестве
 */
@Getter
public class StockItemQuantityDto {

    /**
     * Конструктор для инициализации данных об ID позиции на складе и ее количестве.
     *
     * @param stockItemId ID позиции на складе
     * @param quantity    количество в указанных единицах
     */
    public StockItemQuantityDto(long stockItemId, double quantity) {
        this.stockItemId = stockItemId;
        this.quantity = quantity;
    }

    /**
     * ID позиции на складе
     */
    @Positive
    private long stockItemId;

    /**
     * Количество в граммах, миллилитрах или штуках (целое число)
     */
    @DecimalMin(value = "0.1")
    private double quantity;

}
