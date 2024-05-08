package com.kirin.outlet.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Данные для создания заказа
 */
@Getter
@ToString
public class OrderDto {

    /**
     * Конструктор для инициализации данных для создания заказа.
     * @param receiptId ID чека
     * @param shoppingCartItems список позиций в чеке
     */
    public OrderDto(long receiptId, List<ShopCartItemDto> shoppingCartItems) {
        this.receiptId = receiptId;
        this.shoppingCartItems = shoppingCartItems;
    }

    /**
     * ID чека
     */
    @Positive
    private long receiptId;

    /**
     * Список позиций в чеке: ID позиции меню и количество данной позиции в чеке в штуках
     * (не менее 1)
     */
    @NotEmpty
    @Valid
    private List<ShopCartItemDto> shoppingCartItems;

}
