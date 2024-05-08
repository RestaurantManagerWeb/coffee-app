package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Метод обработки пищевого продукта
 */
@Entity
@Data
@NoArgsConstructor
public class ProcessingMethod {

    /**
     * Конструктор для создания нового метода обработки.
     *
     * @param name        имя метода
     * @param description описание (может быть null)
     */
    public ProcessingMethod(String name, String description) {
        this.name = name;
        if (description != null && description.length() > 0) {
            this.description = description;
        }
    }

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
