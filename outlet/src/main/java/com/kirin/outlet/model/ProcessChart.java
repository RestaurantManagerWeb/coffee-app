package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Технологическая карта
 */
@Entity
@Data
public class ProcessChart {

    /**
     * Уникальный идентификатор техкарты
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Описание приготовления, не обязательное поле
     */
    @Column(columnDefinition = "text")
    private String description;

    /**
     * Выход в граммах
     */
    @Column(nullable = false, columnDefinition = "decimal(5,1)")
    private BigDecimal yield;

    /**
     * На сколько порций рассчитана техкарта, по умолчанию 1
     */
    @ColumnDefault(value = "1")
    @Column(nullable = false, columnDefinition = "smallint")
    private Integer portion;

    /**
     * Список рецептурных компонентов. Двунаправленная связь OneToMany для получения списка
     * рецептурных компонентов и их количества
     */
    @JsonIgnore
    @OneToMany(mappedBy="processChart", fetch = FetchType.LAZY)
    private Set<RecipeComposition> recipeCompositions;
}
