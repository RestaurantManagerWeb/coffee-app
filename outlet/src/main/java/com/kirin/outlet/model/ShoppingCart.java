package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

/**
 * Корзина для формирования заказа
 */
@Entity
@Data
@NoArgsConstructor
public class ShoppingCart implements Persistable<ShoppingCartPK> {

    /**
     * Конструктор для создания позиции в заказе. Перед сохранением нового объекта в базе
     * ID (составной PK) помечается как "новый" (insert без предварительного select).
     * @param orderingId ID заказа
     * @param menuItemId ID позиции меню
     * @param quantity количество позиций в заказе в штуках
     */
    public ShoppingCart(Long orderingId, Long menuItemId, Integer quantity) {
        shoppingCartPK = new ShoppingCartPK(orderingId, menuItemId);
        this.quantity = quantity;
        isNew = true;
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
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", insertable=false, updatable=false,
            foreignKey = @ForeignKey(name = "shopping_cart_miid_fk"))
    private MenuItem menuItem;

    /**
     * Данные о заказе. Двунаправленная связь ManyToOne с сущностью заказа
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordering_id", insertable=false, updatable=false,
            foreignKey = @ForeignKey(name = "shopping_cart_oid_fk"))
    private Ordering ordering;

    /**
     * Состояние объекта (новый или ранее сохраненный). Не сериализуется и не хранится в базе.
     */
    @ToString.Exclude
    private transient boolean isNew;

    /**
     * Новые объекты после добавления в базу и загруженные из базы объекты отмечаются как
     * ранее сохраненные (при их изменении и повторном сохранении в базу будет вызван update)
     */
    @PrePersist
    @PostLoad
    void trackNotNew() {
        this.isNew = false;
    }

    /**
     * Получение ID (составного первичного ключа)
     * @return ключ
     */
    @Override
    public ShoppingCartPK getId() {
        return shoppingCartPK;
    }
}
