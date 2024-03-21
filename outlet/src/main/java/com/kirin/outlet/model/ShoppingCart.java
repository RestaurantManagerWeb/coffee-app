package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

/**
 * Корзина для формирования заказа
 */
@Entity
@Data
public class ShoppingCart {

    /**
     * Встраиваемый класс, хранящий связь с сущностями позиции меню и заказом
     */
    @EmbeddedId
    private ShoppingCartPK shoppingCartPK;

    /**
     * Количество указанной позиции меню в заказе в штуках
     */
    @Column(nullable = false, columnDefinition = "tinyint")
    private Integer quantity;

}
