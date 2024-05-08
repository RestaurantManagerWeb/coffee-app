package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    /**
     * Получение неудаленной позиции меню по ID.
     *
     * @param id уникальный идентификатор позиции меню
     * @return Optional объект позиции меню
     */
    Optional<MenuItem> findByIdAndDeletedAtIsNull(Long id);

    /**
     * Получение списка неудаленных позиций меню, у которых связь с позицией на складе
     * не равна null.
     * @return список неудаленных позиций меню, связанных с позициями на складе
     */
    List<MenuItem> findByStockItemIdIsNotNullAndDeletedAtIsNull();

    /**
     * Получение списка неудаленных позиций меню с указанными ID.
     *
     * @param id множество ID
     * @return список позиций меню с указанными ID
     */
    List<MenuItem> findByDeletedAtIsNullAndIdIn(Set<Long> id);

}
