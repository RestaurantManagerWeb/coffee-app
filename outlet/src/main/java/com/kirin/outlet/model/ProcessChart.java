package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
public class ProcessChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, columnDefinition = "decimal(5,1)")
    private BigDecimal yield;

    @OneToMany(mappedBy="processChart")
    private Set<RecipeComposition> recipeCompositions;
}
