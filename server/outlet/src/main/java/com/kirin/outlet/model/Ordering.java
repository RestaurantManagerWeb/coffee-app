package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Заказ
 */
@Entity
@Data
@NoArgsConstructor
public class Ordering {

    /**
     * Конструктор для создания нового заказа по ID чека с заполнением текущих
     * даты и времени создания.
     *
     * @param receiptId уникальный идентификатор чека
     */
    public Ordering(Long receiptId) {
        this.receiptId = receiptId;
    }

    /**
     * Уникальный идентификатор заказа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания заказа, по умолчанию текущее время. Нельзя изменить.
     */
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    /**
     * Дата и время отмены заказа, может быть null
     */
    @Column(insertable = false)
    private Timestamp cancelledAt;

    /**
     * Уникальный идентификатор чека, сформированного в другом сервисе
     */
    @Column(nullable = false, unique = true, updatable = false)
    private Long receiptId;

    /**
     * Список позиций в заказе. Двунаправленная связь OneToMany для получения списка
     * позиций меню в заказе и их количества
     */
    @OneToMany(mappedBy = "ordering", fetch = FetchType.LAZY)
    private List<ShoppingCart> shoppingCarts;

}
