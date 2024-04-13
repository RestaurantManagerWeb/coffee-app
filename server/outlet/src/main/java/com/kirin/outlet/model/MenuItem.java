package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

/**
 * Позиция меню. Например, капучино 200 мл, капучино 300 мл
 */
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
public class MenuItem extends SoftDeletes {

    /**
     * Конструктор для создания позиции меню, связанной со штучной позицией на складе.
     *
     * @param name        название позиции
     * @param price       цена в рублях
     * @param vat         НДС в %. По умолчанию 0%
     * @param menuGroupId ID связанной группы меню
     * @param stockItemId ID связанной позиции на складе
     */
    public MenuItem(String name, BigDecimal price, Integer vat, Integer menuGroupId, Long stockItemId) {
        this.name = name;
        this.price = price;
        if (vat != null) this.vat = vat;
        this.menuGroupId = menuGroupId;
        this.stockItemId = stockItemId;
    }

    /**
     * Конструктор для создания позиции меню, связанной с техкартой.
     *
     * @param name           название позиции
     * @param price          цена в рублях
     * @param vat            НДС в %. По умолчанию 0%
     * @param processChartId ID связанной техкарты
     * @param menuGroupId    ID связанной группы меню
     */
    public MenuItem(String name, BigDecimal price, Integer vat, Long processChartId, Integer menuGroupId) {
        this.name = name;
        this.price = price;
        if (vat != null) this.vat = vat;
        this.menuGroupId = menuGroupId;
        this.processChartId = processChartId;
    }

    /**
     * Уникальный идентификатор позиции меню
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название позиции
     */
    @Column(nullable = false, columnDefinition = "varchar(50)")
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
    private Boolean inStopList = false;

    /**
     * Группа меню. Однонаправленная связь ManyToOne с сущностью группы меню.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_group_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "menu_item_mgid_fk"))
    private MenuGroup menuGroup;

    /**
     * Уникальный идентификатор связанной группы меню
     */
    @Column(name = "menu_group_id")
    private Integer menuGroupId;

    /**
     * Позиция на складе (опционально).
     * Однонаправленная связь ManyToOne с сущностью позиции на складе. Значение должно
     * быть уникальным среди неудаленных позиций меню. Если сущность позиции меню
     * связана с технологической картой, то данная связь должна быть null.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_item_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "menu_item_siid_fk"))
    private StockItem stockItem;

    /**
     * Уникальный идентификатор связанной позиции на складе
     */
    @Column(name = "stock_item_id")
    private Long stockItemId;

    /**
     * Технологическая карта (опционально).
     * Однонаправленная связь ManyToOne с сущностью технологической карты.
     * Значение должно быть уникальным среди неудаленных позиций меню. Если сущность
     * позиции меню связана с позицией на складе, то данная связь должна быть null.
     */
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_chart_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "menu_item_pcid_fk"))
    private ProcessChart processChart;

    /**
     * Уникальный идентификатор связанной техкарты
     */
    @Column(name = "process_chart_id")
    private Long processChartId;

}
