package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Пищевой ингредиент
 */
@Entity
@Data
@NoArgsConstructor
public class Ingredient {

    /**
     * Конструктор для создания ингредиента.
     *
     * @param name               уникальное название ингредиента
     * @param processingMethodId ID связанного метода обработки
     * @param weightLoss         процент потерь при обработке
     * @param stockItemId        ID связанной позиции на складе
     */
    public Ingredient(String name, int processingMethodId, int weightLoss, Long stockItemId) {
        this.name = name;
        this.processingMethodId = processingMethodId;
        this.weightLoss = weightLoss;
        this.stockItemId = stockItemId;
    }

    /**
     * Уникальный идентификатор ингредиента
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальное название ингредиента
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String name;

    /**
     * Процент потерь при обработке
     */
    @Column(nullable = false, columnDefinition = "smallint")
    private Integer weightLoss;

    /**
     * Позиция на складе. Однонаправленная связь ManyToOne с сущностью позиции на складе.
     * Может быть null.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_item_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "ingredient_siid_fk"))
    private StockItem stockItem;

    /**
     * Уникальный идентификатор связанной позиции на складе
     */
    @Column(name = "stock_item_id")
    private Long stockItemId;

    /**
     * Метод обработки. Однонаправленная связь ManyToOne с сущностью метода обработки.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "processing_method_id", insertable = false, updatable = false,
            nullable = false, foreignKey = @ForeignKey(name = "ingredient_pmid_fk"))
    private ProcessingMethod processingMethod;

    /**
     * Уникальный идентификатор связанного метода обработки
     */
    @Column(name = "processing_method_id")
    private Integer processingMethodId;

}
