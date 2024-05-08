package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Рецептурный компонент
 */
@Entity
@Data
@NoArgsConstructor
public class RecipeComposition {

    /**
     * Конструктор для создания нового рецептурного компонента.
     *
     * @param processChartId ID связанной техкарты
     * @param netto          норма расхода продукта в граммах или штуках
     * @param ingredientId   ID связанного ингредиента
     * @param semiFinishedId ID связанного полуфабриката
     */
    public RecipeComposition(Long processChartId, BigDecimal netto,
                             Long ingredientId, Long semiFinishedId) {
        this.processChartId = processChartId;
        this.netto = netto;
        this.ingredientId = ingredientId;
        this.semiFinishedId = semiFinishedId;
    }

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
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "recipe_composition_pcid_fk"))
    private ProcessChart processChart;

    /**
     * Уникальный идентификатор связанной техкарты
     */
    @Column(name = "process_chart_id")
    private Long processChartId;

    /**
     * Ингредиент (опционально).
     * Однонаправленная связь ManyToOne с сущностью ингредиента.
     * Значения могут повторяться. Если сущность рецептурного компонента
     * связана с полуфабрикатом, то данная связь должна быть null.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "recipe_composition_iid_fk"))
    private Ingredient ingredient;

    /**
     * Уникальный идентификатор связанного ингредиента
     */
    @Column(name = "ingredient_id")
    private Long ingredientId;

    /**
     * Полуфабриакат (опционально).
     * Однонаправленная связь ManyToOne с сущностью полуфабриката.
     * Значения могут повторяться. Если сущность рецептурного компонента
     * связана с ингредиентом, то данная связь должна быть null.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "semi_finished_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "recipe_composition_sfid_fk"))
    private SemiFinished semiFinished;

    /**
     * Уникальный идентификатор связанного полуфабриката
     */
    @Column(name = "semi_finished_id")
    private Long semiFinishedId;

}
