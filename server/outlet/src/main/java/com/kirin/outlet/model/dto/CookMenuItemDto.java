package com.kirin.outlet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Информация о названии позиции меню или полуфабриката и ID техкарты
 */
@Getter
@AllArgsConstructor
public class CookMenuItemDto {

    /**
     * ID позиции меню
     */
    private long id;

    /**
     * Название позиции меню или полуфабриката
     */
    private String name;

    /**
     * ID техкарты
     */
    private long processChartId;

}
