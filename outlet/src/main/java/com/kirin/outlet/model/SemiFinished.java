package com.kirin.outlet.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SemiFinished {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", unique = true, nullable = false,
            foreignKey = @ForeignKey(name = "semi_finished_pcid_fk"))
    private ProcessChart processChart;

}
