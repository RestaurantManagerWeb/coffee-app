package com.kirin.outlet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * Название группы и входящие позиции
 */
@Getter
@AllArgsConstructor
public class CookGroupDto {

    /**
     * Название группы
     */
    private String name;

    /**
     * Входящие в группу позиции, которые готовятся на предприятии
     */
    private List<CookItemDto> items;

    /**
     * Конструктор для инициализации пустого списка позиций
     */
    public CookGroupDto() {
        this.items = new LinkedList<>();
    }

    /**
     * Конструктор для задания списка позиций с одним указанным элементом.
     * @param newItem позиция для добавления в список
     */
    public CookGroupDto(CookItemDto newItem) {
        this();
        addItem(newItem);
    }

    /**
     * Добавление новой позиции в список.
     * @param item позиция для добавления в список
     */
    public void addItem(CookItemDto item) {
        items.add(item);
    }

    /**
     * Задание названия группы.
     * @param nameGroup название группы
     */
    public void setName(String nameGroup) {
        name = nameGroup;
    }

}
