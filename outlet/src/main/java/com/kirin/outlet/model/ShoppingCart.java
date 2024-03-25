package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Корзина для формирования заказа
 */
@Entity
@Data
@NoArgsConstructor
public class ShoppingCart {

    /**
     * Конструктор для создания позиции в заказе.
     * @param orderingId ID заказа
     * @param menuItemId ID позиции меню
     * @param quantity количество позиций в заказе в штуках
     */
    public ShoppingCart(Long orderingId, Long menuItemId, Integer quantity) {
        shoppingCartPK = new ShoppingCartPK(orderingId, menuItemId);
        this.quantity = quantity;
    }

    /**
     * Встраиваемый класс, хранящий идентификаторы позиции меню и заказа
     */
    @EmbeddedId
    private ShoppingCartPK shoppingCartPK;

    /**
     * Количество указанной позиции меню в заказе в штуках
     */
    @Column(nullable = false, columnDefinition = "smallint")
    private Integer quantity;

    /**
     * Позиция меню. Однонаправленная связь ManyToOne с сущностью позиции меню
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", insertable=false, updatable=false,
            foreignKey = @ForeignKey(name = "shopping_cart_miid_fk"))
    @JsonIgnore
    private MenuItem menuItem;

    /**
     * Данные о заказе. Двунаправленная связь ManyToOne с сущностью заказа
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordering_id", insertable=false, updatable=false,
            foreignKey = @ForeignKey(name = "shopping_cart_oid_fk"))
    @JsonIgnore
    private Ordering ordering;

}
