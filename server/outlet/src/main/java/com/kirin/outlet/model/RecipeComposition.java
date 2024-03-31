package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Рецептурный компонент
 */
@Entity
@Data
public class RecipeComposition {

    /**
     * Уникальный идентификатор рецептурного компонента
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Норма расхода продукта в граммах или штуках (для штучных позиций на складе)
     */
    @Column(nullable = false, columnDefinition = "decimal(5,1)")
    private BigDecimal netto;

    /**
     * Технологическая карта, к которой относится данный компонент. Двунаправленная связь
     * ManyToOne с сущностью технологической карты.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", nullable = false,
            foreignKey = @ForeignKey(name = "recipe_composition_pcid_fk"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProcessChart processChart;

    /**
     * Ингредиент (опционально).
     * Однонаправленная связь ManyToOne с сущностью ингредиента.
     * Значения могут повторяться. Если сущность рецептурного компонента
     * связана с полуфабрикатом, то данная связь должна быть null.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // optional = true
    @JoinColumn(name = "ingredient_id",
            foreignKey = @ForeignKey(name = "recipe_composition_iid_fk"))
    private Ingredient ingredient;

    /**
     * Полуфабриакат (опционально).
     * Однонаправленная связь ManyToOne с сущностью полуфабриката.
     * Значения могут повторяться. Если сущность рецептурного компонента
     * связана с ингредиентом, то данная связь должна быть null.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // optional = true
    @JoinColumn(name = "semi_finished_id",
            foreignKey = @ForeignKey(name = "recipe_composition_sfid_fk"))
    private SemiFinished semiFinished;

}
