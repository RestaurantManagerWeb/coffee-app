package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Полуфабрикат
 */
@Entity
@Data
@NoArgsConstructor
public class SemiFinished {

    /**
     * Конструктор для создания нового полуфабриката.
     *
     * @param name           уникальное название полуфабриката
     * @param processChartId ID связанной техкарты
     */
    public SemiFinished(String name, Long processChartId) {
        this.name = name;
        this.processChartId = processChartId;
    }

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
    @ToString.Exclude
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", unique = true, nullable = false,
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "semi_finished_pcid_fk"))
    private ProcessChart processChart;

    /**
     * Уникальный идентификатор связанной техкарты
     */
    @Column(name = "process_chart_id")
    private Long processChartId;

}
