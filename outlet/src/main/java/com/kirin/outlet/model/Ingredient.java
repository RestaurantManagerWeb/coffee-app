package com.kirin.outlet.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String name;

    @Column(nullable = false, columnDefinition = "tinyint")
    private Integer weightLoss;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_item_id", nullable = false,
            foreignKey = @ForeignKey(name = "ingredient_siid_fk"))
    private StockItem stockItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "processing_method_id", nullable = false,
            foreignKey = @ForeignKey(name = "ingredient_pmid_fk"))
    private ProcessingMethod processingMethod;

}
