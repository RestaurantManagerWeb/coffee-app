package com.kirin.outlet.service;

import com.kirin.outlet.model.Ingredient;
import com.kirin.outlet.model.ProcessingMethod;
import com.kirin.outlet.model.dto.IngredientDto;
import com.kirin.outlet.model.dto.ProcessingMethodDto;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.IngredientRepo;
import com.kirin.outlet.repository.ProcessingMethodRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Проверка наличия всех указанных ингредиентов по их ID.
     *
     * @param idSet множество ID ингредиентов для проверки
     */
    public void checkPresentAllIngredientsById(Set<Long> idSet) {
        List<Ingredient> ingredients = ingredientRepo.findByIdIn(idSet);
        if (ingredients.size() < idSet.size()) {
            idSet.removeAll(ingredients.stream()
                    .mapToLong(Ingredient::getId)
                    .boxed().collect(Collectors.toSet()));
            throw new ItemNotFoundException("Не найдены ингредиенты с ID = " + idSet);
        }
    }

    /**
     * Получение списка всех методов обработки.
     *
     * @return список методов обработки
     */
    public List<ProcessingMethod> getProcessingMethodsList() {
        return processingMethodRepo.findAll();
    }

    /**
     * Создание нового метода обработки.
     *
     * @param dto имя и описание метода
     * @return созданный метод обработки
     */
    public ProcessingMethod createProcessingMethod(@Valid ProcessingMethodDto dto) {
        checkUniqueProcessingMethodName(dto.getName());
        return processingMethodRepo.save(
                new ProcessingMethod(dto.getName().toLowerCase(), dto.getDescription()));
    }

    /**
     * Проверка на отсутствие в репозитории метода обработки с указанным именем
     * (игнорируя регистр).
     *
     * @param name проверяемое имя
     */
    private void checkUniqueProcessingMethodName(String name) {
        List<ProcessingMethod> items = processingMethodRepo.findByNameIgnoreCase(name);
        if (items.size() > 0) throw new IncorrectRequestDataException(
                "checkUniqueProcessingMethodName.name", "Имя метода обработки не уникально");
    }

    /**
     * Создание нового ингредиента.
     *
     * @param dto данные для создания ингредиента
     * @return созданный ингредиент
     */
    public Ingredient createIngredient(@Valid IngredientDto dto) {
        checkUniqueIngredientName(dto.getName());
        getProcessingMethodById(dto.getProcessingMethodId());
        if (dto.getStockItemId() != null)
            stockService.getStockItemById(dto.getStockItemId());
        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        return ingredientRepo.save(new Ingredient(name, dto.getProcessingMethodId(),
                dto.getWeightLoss(), dto.getStockItemId()));
    }

    /**
     * Проверка на отсутствие в репозитории ингредиента с указанным именем (игнорируя регистр).
     *
     * @param name проверяемое имя
     */
    private void checkUniqueIngredientName(String name) {
        List<Ingredient> items = ingredientRepo.findByNameIgnoreCase(name);
        if (items.size() > 0) throw new IncorrectRequestDataException(
                "checkUniqueIngredientName.name", "Имя ингредиента не уникально");
    }

}
