package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Метод обработки пищевого продукта
 */
@Entity
@Data
public class ProcessingMethod {

    /**
     * Уникальный идентификатор метода обработки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Уникальное название метода обработки
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(30)")
    private String name;

    /**
     * Описание (не обязательно)
     */
    @Column(columnDefinition = "text")
    private String description;

}
