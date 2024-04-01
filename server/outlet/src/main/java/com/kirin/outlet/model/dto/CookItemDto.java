package com.kirin.outlet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Информация о названии позиции меню или полуфабриката и ID техкарты
 */
@Getter
@AllArgsConstructor
public class CookItemDto {

    /**
     * Название позиции меню или полуфабриката
     */
    private String itemName;

    /**
     * ID техкарты
     */
    private Long processChartId;

}
