package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Единицы измерения
 */
@Entity
@Data
public class UnitMeasure {

    /**
     * Уникальный идентификатор единицы измерения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Уникальное название группы единиц измерения (масса, объем, количество)
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)",
            insertable = false, updatable = false) /////
    private String name;

    /**
     * Символьное обозначение (г, мл, шт.)
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(5)",
            insertable = false, updatable = false) /////
    private String symbol;
}
