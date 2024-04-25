package com.kirin.outlet.repository;

import com.kirin.outlet.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface IngredientRepo extends JpaRepository<Ingredient, Long> {

    /**
     * Поиск ингредиентов по имени, игнорируя регистр.
     *
     * @param name имя ингредиента для поиска
     * @return список ингредиентов с указанным именем
     */
    List<Ingredient> findByNameIgnoreCase(String name);

    /**
     * Получение списка ингредиентов с указанными ID.
     *
     * @param id множество ID
     * @return список ингредиентов с указанными ID
     */
    List<Ingredient> findByIdIn(Set<Long> id);

}
