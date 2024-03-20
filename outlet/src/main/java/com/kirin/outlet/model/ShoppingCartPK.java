package com.kirin.outlet.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Составной первичный ключ для ShoppingCart
 */
@Embeddable
@Data
public class ShoppingCartPK {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false,
            foreignKey = @ForeignKey(name = "shopping_cart_miid_fk"))
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordering_id", nullable = false,
            foreignKey = @ForeignKey(name = "shopping_cart_oid_fk"))
    private Ordering ordering;

}
