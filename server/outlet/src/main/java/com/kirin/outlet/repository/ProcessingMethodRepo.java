package com.kirin.outlet.repository;

import com.kirin.outlet.model.ProcessingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessingMethodRepo extends JpaRepository<ProcessingMethod, Integer> {

    /**
     * Поиск метода обработки по имени, игнорируя регистр.
     *
     * @param name имя метода для поиска
     * @return список методов обработки с указанным именем
     */
    List<ProcessingMethod> findByNameIgnoreCase(String name);

}
