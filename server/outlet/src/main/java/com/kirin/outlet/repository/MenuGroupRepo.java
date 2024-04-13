package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuGroupRepo extends JpaRepository<MenuGroup, Integer> {

    /**
     * Поиск группы меню по имени, игнорируя регистр.
     *
     * @param name имя группы для поиска
     * @return список групп меню с указанным именем
     */
    List<MenuGroup> findByNameIgnoreCase(String name);

    /**
     * Поиск неудаленной группы по ID.
     *
     * @param id ID группы меню
     * @return Optional объект группы меню
     */
    Optional<MenuGroup> findByIdAndDeletedAtIsNull(Integer id);

    /**
     * Получение всех групп меню, у которых не задано время удаления.
     *
     * @return список неудаленных групп меню
     */
    List<MenuGroup> findAllByDeletedAtIsNull();

}
