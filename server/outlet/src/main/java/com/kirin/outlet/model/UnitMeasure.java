package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Единицы измерения
 */
@Entity
@Data
@NoArgsConstructor
public class UnitMeasure {

    /**
     * Конструктор для создания новой единицы измерения.
     *
     * @param name   Уникальное название группы единиц измерения
     * @param symbol Символьное обозначение
     */
    public UnitMeasure(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * Уникальный идентификатор единицы измерения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Уникальное название группы единиц измерения
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)", updatable = false)
    private String name;

    /**
     * Символьное обозначение
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(5)", updatable = false)
    private String symbol;

}
