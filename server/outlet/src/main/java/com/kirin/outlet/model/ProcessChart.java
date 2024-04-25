package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

/**
 * Технологическая карта
 */
@Entity
@Data
@NoArgsConstructor
public class ProcessChart {

    /**
     * Конструктор для создания новой техкарты.
     *
     * @param description описание приготовления, может быть null
     * @param yield       выход в граммах
     * @param portion     количество порций, по умолчанию 1
     */
    public ProcessChart(String description, int yield, Integer portion) {
        this.description = description;
        this.yield = yield;
        if (portion != null) this.portion = portion;
    }

    /**
     * Уникальный идентификатор техкарты
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Описание приготовления, может быть null
     */
    @Column(columnDefinition = "text")
    private String description;

    /**
     * Выход в граммах
     */
    @Column(nullable = false)
    private Integer yield;

    /**
     * На сколько порций рассчитана техкарта, по умолчанию 1
     */
    @ColumnDefault(value = "1")
    @Column(nullable = false, columnDefinition = "smallint")
    private Integer portion = 1;

    /**
     * Список рецептурных компонентов. Двунаправленная связь OneToMany для получения списка
     * рецептурных компонентов и их количества
     */
    @JsonIgnore
    @OneToMany(mappedBy = "processChart", orphanRemoval = true,
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecipeComposition> recipeCompositions;

}
