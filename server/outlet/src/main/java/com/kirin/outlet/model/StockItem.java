package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Позиция на складе
 */
@Entity
@Data
public class StockItem {

    /**
     * Уникальный идентификатор позиции на складе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальное название позиции на складе
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(100)")
    private String name;

    /**
     * Количество в указанных единицах
     */
    @Column(nullable = false, columnDefinition = "decimal(7,1)")
    private BigDecimal quantity;

    /**
     * Единица измерения количества. Однонаправленная связь ManyToOne с сущностью
     * единицы измерения.
     */
    @JsonProperty("unitMeasureId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_measure_id", nullable = false,
            foreignKey = @ForeignKey(name = "stock_item_umid_fk"))
    private UnitMeasure unitMeasure;

}
