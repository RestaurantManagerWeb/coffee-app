package com.kirin.outlet.service;

import com.kirin.outlet.model.Ingredient;
import com.kirin.outlet.model.ProcessingMethod;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.IngredientRepo;
import com.kirin.outlet.repository.ProcessingMethodRepo;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления данными об ингредиентах
 */
@Validated
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final ProcessingMethodRepo processingMethodRepo;

    private final IngredientRepo ingredientRepo;

    private final StockService stockService;

    /**
     * Получение информации о методе обработки по ID.
     *
     * @param id уникальный идентификатор метода обработки продуктов
     * @return информацию о найденном методе обработки
     */
    public ProcessingMethod getProcessingMethodById(@Positive int id) {
        Optional<ProcessingMethod> processingMethod = processingMethodRepo.findById(id);
        if (processingMethod.isEmpty())
            throw new ItemNotFoundException("Метод обработки с ID = " + id + " не найден");
        return processingMethod.get();
    }

    /**
     * Получение ингредиента по ID.
     *
     * @param id уникальный идентификатор ингредиента
     * @return найденный ингредиент
     */
    public Ingredient getIngredientById(@Positive long id) {
        Optional<Ingredient> ingredient = ingredientRepo.findById(id);
        if (ingredient.isEmpty())
            throw new ItemNotFoundException("Ингредиент с ID = " + id + " не найден");
        return ingredient.get();
    }

    /**
     * Получение списка всех ингредиентов, отсортированного по имени.
     *
     * @return отсортированный список ингредиентов
     */
    public List<Ingredient> getIngredientsList() {
        List<Ingredient> ingredients = ingredientRepo.findAll();
        ingredients.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return ingredients;
    }

    /**
     * Получение списка всех методов обработки.
     *
     * @return список методов обработки
     */
    public List<ProcessingMethod> getProcessingMethodsList() {
        return processingMethodRepo.findAll();
    }

}
