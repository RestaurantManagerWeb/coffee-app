package com.kirin.outlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UnitMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)",
            insertable = false, updatable = false) /////
    private String name;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(5)",
            insertable = false, updatable = false) /////
    private String symbol;
}
