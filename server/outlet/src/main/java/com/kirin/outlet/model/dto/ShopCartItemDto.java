package com.kirin.outlet.model.dto;

import lombok.Getter;

/**
 * Данные о позиции в чеке
 */
@Getter
public class ShopCartItemDto {

    /**
     * ID позиции меню
     */
    private long menuItemId;

    /**
     * Количество в штуках
     */
    private int quantity;
}
