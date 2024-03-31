package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Составной первичный ключ для ShoppingCart
 */
@Embeddable
@Data
@NoArgsConstructor
public class ShoppingCartPK {

    /**
     * Конструктор для задания идентификаторов позиции меню и заказа
     * @param orderingId уникальный идентификатор связанной позиции меню
     * @param menuItemId уникальный идентификатор связанного заказа
     */
    public ShoppingCartPK(Long orderingId, Long menuItemId) {
        this.orderingId = orderingId;
        this.menuItemId = menuItemId;
    }

    /**
     * Уникальный идентификатор связанной позиции меню
     */
    @Column(name = "menu_item_id", nullable = false)
    private Long menuItemId;

    /**
     * Уникальный идентификатор связанного заказа
     */
    @Column(name = "ordering_id", nullable = false)
    private Long orderingId;

}
