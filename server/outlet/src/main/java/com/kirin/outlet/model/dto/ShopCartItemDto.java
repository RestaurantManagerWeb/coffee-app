package com.kirin.outlet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Данные о позиции в чеке
 */
@Getter
@AllArgsConstructor
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
