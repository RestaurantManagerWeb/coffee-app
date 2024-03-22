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

/**
 * Заказ
 */
@Entity
@Data
public class Ordering {

    /**
     * Уникальный идентификатор заказа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания заказа, обязательно к заполнению
     */
    @Column(nullable = false, insertable = false, updatable = false)
    private Timestamp createdOn;

    /**
     * Дата и время отмены заказа, может быть null
     */
    private Timestamp cancelledOn;

    /**
     * Уникальный идентификатор чека, сформированного в другом сервисе, может быть null
     */
    private Long receiptId;

    /**
     * Список позиций в заказе. Двунаправленная связь OneToMany для получения списка
     * позиций меню в заказе и их количества
     */
    @OneToMany(mappedBy="shoppingCartPK.ordering")
    private Set<ShoppingCart> shoppingCarts;
}
