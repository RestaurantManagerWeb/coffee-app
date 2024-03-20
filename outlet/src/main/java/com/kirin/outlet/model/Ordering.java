package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
public class Ordering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, insertable = false, updatable = false)
    private Timestamp createdOn;

    @Column()
    private Timestamp cancelledOn;

    @Column()
    private Long receiptId;

    @OneToMany(mappedBy="shoppingCartPK.ordering")
    private Set<ShoppingCart> shoppingCarts;
}
