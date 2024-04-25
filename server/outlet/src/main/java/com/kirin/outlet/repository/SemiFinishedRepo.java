package com.kirin.outlet.repository;

import com.kirin.outlet.model.SemiFinished;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SemiFinishedRepo extends JpaRepository<SemiFinished, Long> {

    /**
     * Поиск полуфабрикатов по имени, игнорируя регистр.
     *
     * @param name имя полуфабриката для поиска
     * @return список полуфабрикатов с указанным именем
     */
    List<SemiFinished> findByNameIgnoreCase(String name);

    /**
     * Получение списка полуфабрикатов с указанными ID.
     *
     * @param id множество ID
     * @return список полуфабрикатов с указанными ID
     */
    List<SemiFinished> findByIdIn(Set<Long> id);

}
