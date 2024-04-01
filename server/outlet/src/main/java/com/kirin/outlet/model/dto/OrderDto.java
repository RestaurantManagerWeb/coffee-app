package com.kirin.outlet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Данные для создания заказа
 */
@Getter
@ToString
@AllArgsConstructor
public class OrderDto {

    /**
     * Уникальный идентификатор чека
     */
    private long receiptId;

    /**
     * Список позиций в чеке: ID позиции меню и количество данной позиции в чеке в штуках
     * (не менее 1)
     */
    private List<ShopCartItemDto> shoppingCartItems;

}
