package com.kirin.outlet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String name;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "tinyint")
    private Integer vat;

    @Column(nullable = false)
    private Boolean inStopList = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_group_id", nullable = false,
            foreignKey = @ForeignKey(name = "menu_item_mgid_fk"))
    // @JsonIgnore
    private MenuGroup menuGroup;

    @OneToOne(fetch = FetchType.LAZY) // optional = true, unique
    @JoinColumn(name = "stock_item_id", foreignKey = @ForeignKey(name = "menu_item_siid_fk"))
    private StockItem stockItem;

    @OneToOne(fetch = FetchType.LAZY) // optional = true, unique
    @JoinColumn(name = "process_chart_id", foreignKey = @ForeignKey(name = "menu_item_pcid_fk"))
    private ProcessChart processChart;

}
