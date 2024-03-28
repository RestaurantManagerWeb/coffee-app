package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

/**
 * Позиция меню. Например, капучино 200 мл, капучино 300 мл
 */
@Entity
@Data
public class MenuItem {

    /**
     * Уникальный идентификатор позиции меню
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальное название позиции
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String name;

    /**
     * Цена в рублях
     */
    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private BigDecimal price;

    /**
     * НДС в %. По умолчанию 0%
     */
    @ColumnDefault(value = "0")
    @Column(nullable = false, columnDefinition = "smallint")
    private Integer vat = 0;

    /**
     * Добавлена ли позиция в стоп-лист. По умолчанию false
     */
    @ColumnDefault(value = "false")
    @Column(nullable = false)
    private Boolean inStopList;

    /**
     * Группа меню. Однонаправленная связь ManyToOne с сущностью группы меню.
     */
    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    // @JsonIgnore
    @JsonProperty("menuGroupId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_group_id", nullable = false,
            foreignKey = @ForeignKey(name = "menu_item_mgid_fk"))
    private MenuGroup menuGroup;

    /**
     * Позиция на складе (опционально).
     * Однонаправленная связь OneToOne с сущностью позиции на складе.
     * Каждое указанное значение должно быть уникальным. Если сущность позиции меню
     * связана с технологической картой, то данная связь должна быть null.
     */
    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    // @JsonIgnore
    @JsonProperty("stockItemId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne(fetch = FetchType.LAZY) // optional = true, unique
    @JoinColumn(name = "stock_item_id", foreignKey = @ForeignKey(name = "menu_item_siid_fk"))
    private StockItem stockItem;

    /**
     * Технологическая карта (опционально).
     * Однонаправленная связь OneToOne с сущностью технологической карты.
     * Каждое указанное значение должно быть уникальным. Если сущность позиции меню
     * связана с позицией на складе, то данная связь должна быть null.
     */
    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    // @JsonIgnore
    @JsonProperty("processChartId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne(fetch = FetchType.LAZY) // optional = true, unique
    @JoinColumn(name = "process_chart_id", foreignKey = @ForeignKey(name = "menu_item_pcid_fk"))
    private ProcessChart processChart;

}
