package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuGroupRepo extends JpaRepository<MenuGroup, Integer> {

    /**
     * Поиск группы меню по имени, игнорируя регистр
     * @param name имя группы для поиска
     * @return Optional объект группы меню
     */
    Optional<MenuGroup> findByNameIgnoreCase(String name);

}
