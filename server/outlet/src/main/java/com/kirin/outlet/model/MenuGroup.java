package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Группа меню
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

}
