package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Полуфабрикат
 */
@Entity
@Data
public class SemiFinished {

    /**
     * Уникальный идентификатор полуфабриката
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальное название полуфабриката
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String name;

    /**
     * Технологическая карта.
     * Однонаправленная связь OneToOne с сущностью технологической карты.
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", unique = true, nullable = false,
            foreignKey = @ForeignKey(name = "semi_finished_pcid_fk"))
    private ProcessChart processChart;

}
