package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

    /**
     * Найти все позиции меню с указанным groupId.
     * @param groupId идентификатор группы меню
     * @return список позиций в указанной группе меню
     */
    List<MenuItem> findByMenuGroupId(Integer groupId);

}
