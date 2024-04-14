package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_measure_id", nullable = false, insertable=false, updatable=false,
            foreignKey = @ForeignKey(name = "stock_item_umid_fk"))
    private UnitMeasure unitMeasure;

    /**
     * Уникальный идентификатор единицы измерения
     */
    @Column(name = "unit_measure_id", nullable = false)
    private Long unitMeasureId;

}
