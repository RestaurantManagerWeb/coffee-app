package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

    /**
     * Найти все позиции меню с указанным groupId, у которых не задано время удаления.
     *
     * @param groupId идентификатор группы меню
     * @return список неудаленных позиций в указанной группе меню
     */
    List<MenuItem> findByMenuGroupIdAndDeletedAtIsNull(Integer groupId);

    /**
     * Поиск позиции меню по имени, игнорируя регистр.
     *
     * @param name имя позиции для поиска
     * @return список позиций меню с указанным именем
     */
    List<MenuItem> findByNameIgnoreCase(String name);

    /**
     * Поиск неудаленной позиции меню по имени, игнорируя регистр.
     *
     * @param name имя позиции для поиска
     * @return список позиций меню с указанным именем
     */
    List<MenuItem> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    /**
     * Поиск активной позиции меню, связанной с указанной позицией на складе.
     *
     * @param stockItemId ID позиции на складе
     * @return список неудаленных позиций меню с указанным ID позиции на складе
     */
    List<MenuItem> findByStockItemIdAndDeletedAtIsNull(Long stockItemId);

    /**
     * Получение всех позиции меню, у которых не задано время удаления.
     *
     * @return список неудаленных позиций меню
     */
    List<MenuItem> findAllByDeletedAtIsNull();

}
