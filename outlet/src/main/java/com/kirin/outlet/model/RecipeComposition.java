package com.kirin.outlet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class RecipeComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "decimal(5,1)")
    private BigDecimal netto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", nullable = false,
            foreignKey = @ForeignKey(name = "recipe_composition_pcid_fk"))
    private ProcessChart processChart;

    @OneToOne(fetch = FetchType.LAZY) // optional = true
    @JoinColumn(name = "ingredient_id",
            foreignKey = @ForeignKey(name = "recipe_composition_iid_fk"))
    private Ingredient ingredient;

    @OneToOne(fetch = FetchType.LAZY) // optional = true
    @JoinColumn(name = "semi_finished_id",
            foreignKey = @ForeignKey(name = "recipe_composition_sfid_fk"))
    private SemiFinished semiFinished;

}
