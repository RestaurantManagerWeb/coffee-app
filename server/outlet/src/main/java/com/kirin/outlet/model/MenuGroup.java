package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Группы меню. Например, кофе, чай, сэндвичи, покупные товары
 */
@Entity
@Data
public class MenuGroup {

    /**
     * Уникальный идентификатор группы меню
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Уникальное название группы
     */
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(30)")
    private String name;

    /**
     * Дата и время удаления группы меню, может быть null
     */
    @Column(insertable = false)
    private Timestamp deletedAt;
}
