package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Пищевой ингредиент
 */
@Entity
@Data
public class Ingredient {

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
     * Позиция на складе. Однонаправленная связь ManyToOne с сущностью склада.
     * Может быть null у ингредиентов с неучитываемым расходом, например, вода, лед.
     */
    @JsonProperty("stockItemId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_item_id", foreignKey = @ForeignKey(name = "ingredient_siid_fk"))
    private StockItem stockItem;

    /**
     * Метод обработки. Однонаправленная связь ManyToOne с сущностью матода обработки.
     */
    @JsonProperty("processingMethodId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "processing_method_id", nullable = false,
            foreignKey = @ForeignKey(name = "ingredient_pmid_fk"))
    private ProcessingMethod processingMethod;

}
