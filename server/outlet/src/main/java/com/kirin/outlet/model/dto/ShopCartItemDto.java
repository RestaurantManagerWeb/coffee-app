package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.ToString;

/**
 * Данные о позиции в чеке
 */
@Getter
@ToString
public class ShopCartItemDto {

    /**
     * Конструктор для инициализации данных о пробитой позиции в чеке.
     *
     * @param menuItemId ID позиции меню
     * @param quantity   количество в штуках
     */
    public ShopCartItemDto(long menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    /**
     * ID позиции меню
     */
    @Positive
    private long menuItemId;

    /**
     * Количество в штуках
     */
    @Positive
    private int quantity;

}
