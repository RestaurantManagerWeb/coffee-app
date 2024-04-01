package com.kirin.outlet.model.dto;

import lombok.Getter;

/**
 * Данные о позиции на складе и ее количестве
 */
@Getter
public class StockItemDto {

    /**
     * ID позиции на складе
     */
    private long stockItemId;

    /**
     * Количество в граммах, миллилитрах или штуках (целое число)
     */
    private double quantity;

}
