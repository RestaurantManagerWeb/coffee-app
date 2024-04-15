package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

/**
 * Данные о ID позиции на складе и ее количестве
 */
@Getter
public class StockItemQuantityDto {

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
