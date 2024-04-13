package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuGroupRepo extends JpaRepository<MenuGroup, Integer> {

    /**
     * Поиск группы меню по имени, игнорируя регистр.
     *
     * @param name имя группы для поиска
     * @return список групп меню с указанным именем
     */
    List<MenuGroup> findByNameIgnoreCase(String name);

}
