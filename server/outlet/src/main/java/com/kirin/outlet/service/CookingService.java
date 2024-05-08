package com.kirin.outlet.service;

import com.kirin.outlet.model.ProcessChart;
import com.kirin.outlet.model.RecipeComposition;
import com.kirin.outlet.model.SemiFinished;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.dto.ProcessChartDto;
import com.kirin.outlet.model.dto.ProcessChartNewDto;
import com.kirin.outlet.model.dto.RecipeCompositionDto;
import com.kirin.outlet.model.dto.RecipeCompositionNewDto;
import com.kirin.outlet.model.dto.SemiFinishedDto;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.ProcessChartRepo;
import com.kirin.outlet.repository.RecipeCompositionRepo;
import com.kirin.outlet.repository.SemiFinishedRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для работы с техкартами, полуфабрикатами и рецептурными компонентами
 */
@Validated
@Service
@RequiredArgsConstructor
public class CookingService {

    private final ProcessChartRepo processChartRepo;

    private final SemiFinishedRepo semiFinishedRepo;

    private final RecipeCompositionRepo recipeCompositionRepo;

    private final IngredientService ingredientService;

    /**
     * Получение информации о техкарте и требуемых ингредиентах.
     *
     * @param processChartId ID техкарты
     * @return объект с данными о техкарте и списком ингредиентов
     */
    public ProcessChartDto getProcessChartInfo(@Positive long processChartId) {
        ProcessChart processChart = getProcessChartById(processChartId);

        List<RecipeComposition> recipeCompositions = processChart.getRecipeCompositions();
        ProcessChartDto pcDto = new ProcessChartDto();
        pcDto.setProcessChart(processChart);
        pcDto.setComponents(getRecipeCompositionInfo(recipeCompositions));
        return pcDto;
    }

    /**
     * Получение техкарты по ID.
     *
     * @param id уникальный идентификатор техкарты
     * @return найденная техкарта
     */
    private ProcessChart getProcessChartById(long id) {
        Optional<ProcessChart> processChart = processChartRepo.findById(id);
        if (processChart.isEmpty())
            throw new ItemNotFoundException("Техкарта с ID = " + id + " не найдена");
        return processChart.get();
    }

    /**
     * Получение информации о рецептурных компонентах, требующихся для приготовления по техкарте.
     *
     * @param recipeCompositions список рецептурных компонентов
     * @return список с информацией о рецептурных компонентах, отсортированный по ID компонента
     */
    private List<RecipeCompositionDto> getRecipeCompositionInfo(
            List<RecipeComposition> recipeCompositions
    ) {
        List<RecipeCompositionDto> components = new ArrayList<>();
        StockItem stockItem;
        for (RecipeComposition item : recipeCompositions) {
            if (item.getSemiFinished() != null) {
                components.add(new RecipeCompositionDto(item, item.getSemiFinished().getName()));
            } else {
                stockItem = item.getIngredient().getStockItem();
                components.add(new RecipeCompositionDto(item, item.getIngredient().getName(),
                        stockItem != null && stockItem.getUnitMeasure().getId() == 3));
            }
        }
        Collections.sort(components);
        return components;
    }

    /**
     * Получение списка полуфабрикатов, отсортированного по имени.
     *
     * @return список с информацией о полуфабрикатах
     */
    public List<SemiFinished> getSemiFinishedProductsList() {
        List<SemiFinished> semiFinishedList = semiFinishedRepo.findAll();
        semiFinishedList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return semiFinishedList;
    }

    /**
     * Получение полуфабриката по ID.
     *
     * @param id уникальный идентификатор полуфабриката
     * @return найденный полуфабрикат
     */
    public SemiFinished getSemiFinishedById(@Positive long id) {
        Optional<SemiFinished> semiFinished = semiFinishedRepo.findById(id);
        if (semiFinished.isEmpty())
            throw new ItemNotFoundException("Полуфабрикат с ID = " + id + " не найден");
        return semiFinished.get();
    }

    /**
     * Проверка данных и создание нового полуфабриката и его техкарты со списком
     * рецептурных компонентов.
     *
     * @param dto данные для создания полуфабриката, техкарты и рецептурных компонентов
     * @return объект созданного полуфабриката
     */
    public SemiFinished createSemiFinished(@Valid SemiFinishedDto dto) {
        checkUniqueSemiFinishedName(dto.getName());
        checkCorrectRecipeCompositions(dto.getRecipeCompositions());
        try {
            return createSemiFinishedAndProcessChart(dto);
        } catch (Exception e) {
            throw new CannotCreateTransactionException(
                    "Не удалось создать полуфабрикат. " + e.getMessage(), e);
        }
    }

    /**
     * Транзакция. Создание связанных техкарты, рецептурных компонентов и полуфабриката.
     *
     * @param dto данные для создания объектов
     * @return объект созданного полуфабриката
     */
    @Transactional
    private SemiFinished createSemiFinishedAndProcessChart(SemiFinishedDto dto) {
        long pcId = createProcessChartAndRecipeCompositions(
                dto.getProcessChart(), dto.getRecipeCompositions());
        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        return semiFinishedRepo.save(new SemiFinished(name, pcId));
    }

    /**
     * Проверка на отсутствие в репозитории полуфабриката с указанным именем
     * (игнорируя регистр).
     *
     * @param name проверяемое имя
     */
    private void checkUniqueSemiFinishedName(String name) {
        List<SemiFinished> items = semiFinishedRepo.findByNameIgnoreCase(name);
        if (items.size() > 0) throw new IncorrectRequestDataException(
                "checkUniqueSemiFinishedName.name", "Имя полуфабриката не уникально");
    }

    /**
     * Проверка наличия ингредиентов и полуфабрикатов, указанных в качестве рецептурных компонентов.
     *
     * @param recipeCompositions список с данными для создания рецептурных компонентов
     */
    public void checkCorrectRecipeCompositions(List<RecipeCompositionNewDto> recipeCompositions) {
        Set<Long> ingredientsId = new HashSet<>();
        Set<Long> semiFinishedId = new HashSet<>();
        for (RecipeCompositionNewDto rc : recipeCompositions) {
            if (rc.getIngredientId() != null)
                ingredientsId.add(rc.getIngredientId());
            else semiFinishedId.add(rc.getSemiFinishedId());
        }
        if (!ingredientsId.isEmpty())
            ingredientService.checkPresentAllIngredientsById(ingredientsId);
        if (!semiFinishedId.isEmpty())
            checkPresentAllSemiFinishedById(semiFinishedId);
    }

    /**
     * Проверка наличия всех указанных полуфабрикатов по их ID.
     *
     * @param idSet множество ID полуфабрикатов для проверки
     */
    private void checkPresentAllSemiFinishedById(Set<Long> idSet) {
        List<SemiFinished> semiFinishedList = semiFinishedRepo.findByIdIn(idSet);
        if (semiFinishedList.size() < idSet.size()) {
            idSet.removeAll(semiFinishedList.stream()
                    .mapToLong(SemiFinished::getId)
                    .boxed().collect(Collectors.toSet()));
            throw new ItemNotFoundException("Не найдены полуфабрикаты с ID = " + idSet);
        }
    }

    /**
     * Создание техкарты и связанных рецептурных компонентов.
     *
     * @param processChart       данные для создания техкарты
     * @param recipeCompositions данные для создания рецептурных компонентов
     * @return ID созданной техкарты
     */
    public long createProcessChartAndRecipeCompositions(
            ProcessChartNewDto processChart, List<RecipeCompositionNewDto> recipeCompositions
    ) {
        ProcessChart newPC = processChartRepo.save(new ProcessChart(processChart.getDescription(),
                processChart.getYield(), processChart.getPortion()));
        for (RecipeCompositionNewDto rc : recipeCompositions) {
            recipeCompositionRepo.save(new RecipeComposition(newPC.getId(), rc.getNetto(),
                    rc.getIngredientId(), rc.getSemiFinishedId()));
        }
        return newPC.getId();
    }
}
