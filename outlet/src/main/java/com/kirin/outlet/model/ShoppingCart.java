package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ShoppingCart {

    @EmbeddedId
    private ShoppingCartPK shoppingCartPK;

    @Column(nullable = false, columnDefinition = "tinyint")
    private Integer quantity;

}
