package com.kirin.outlet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(100)")
    private String name;

    @Column(nullable = false, columnDefinition = "decimal(7,1)")
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_measure_id", nullable = false,
            foreignKey = @ForeignKey(name = "stock_item_umid_fk"))
    private UnitMeasure unitMeasure;

}
