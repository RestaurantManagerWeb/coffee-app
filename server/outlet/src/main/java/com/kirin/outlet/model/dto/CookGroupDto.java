package com.kirin.outlet.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Название группы и входящие позиции
 */
public class CookGroupDto {

    /**
     * Название группы
     */
    private String name;

    /**
     * Входящие в группу позиции, которые готовятся на предприятии
     */
    private List<CookMenuItemDto> items;

    /**
     * Конструктор для задания списка позиций с одним указанным элементом.
     * Имя не устанавливается.
     * @param newItem позиция для добавления в список
     */
    public CookGroupDto(CookMenuItemDto newItem) {
        this.items = new ArrayList<>();
        addItem(newItem);
    }

    /**
     * Добавление новой позиции в список.
     * @param item позиция для добавления в список
     */
    public void addItem(CookMenuItemDto item) {
        items.add(item);
    }

    /**
     * Задание названия группы.
     * @param nameGroup название группы
     */
    public void setName(String nameGroup) {
        name = nameGroup;
    }

    /**
     * Получение отсортированного по имени списка позиций меню.
     * @return отсортированный список
     */
    public List<CookMenuItemDto> getItems() {
        items.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return items;
    }

    /**
     * Получение имени группы меню
     * @return имя группы
     */
    public String getName() {
        return name;
    }
}
