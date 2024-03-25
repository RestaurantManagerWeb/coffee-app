package com.kirin.outlet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;

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
     * Коллекция, в которой ключем выступает ID позиции меню, а значением - количество
     * данной позиции в чеке в штуках (не менее 1)
     */
    private HashMap<Long, Integer> shoppingCartItems;

}
