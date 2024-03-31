package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.ProcessChart;
import com.kirin.outlet.model.RecipeComposition;
import com.kirin.outlet.model.SemiFinished;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.dto.CookItemDto;
import com.kirin.outlet.model.dto.IngredientOfRecipeDto;
import com.kirin.outlet.model.dto.ProcessChartDto;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.ProcessChartRepo;
import com.kirin.outlet.repository.RecipeCompositionRepo;
import com.kirin.outlet.repository.SemiFinishedRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Сервис для создания и управления позициями меню, включая работу с техкартами,
 * полуфабрикатами и рецептурными компонентами
 */
@Service
@RequiredArgsConstructor
public class CookingService {

    private final ProcessChartRepo processChartRepo;

    private final SemiFinishedRepo semiFinishedRepo;

    private final RecipeCompositionRepo recipeCompositionRepo;

    private final OrderService orderService;

    /**
     * Получение информации о техкарте и требуемых ингредиентах.
     * @param processChartId ID техкарты
     * @return объект с данными о техкарте и списком ингредиентов
     */
    public ProcessChartDto getProcessChartInfo(Long processChartId) {
        Optional<ProcessChart> processChart = processChartRepo.findById(processChartId);
        if (processChart.isEmpty()) {
            throw new ItemNotFoundException("Техкарта с ID = " + processChartId + " не найдена");
        }
        ProcessChartDto pcDto = new ProcessChartDto();
        pcDto.setProcessChart(processChart.get());
        pcDto.setComponents(getRecipeCompositionInfo(processChart.get()));
        return pcDto;
    }

    /**
     * Получение информации о рецептурных компонентах (РК), требующихся для приготовления по
     * техкарте с указанным ID: ID РК (ключ), название, масса нетто РК, для п/ф - ID техкарты,
     * для ингредиента - является ли он штучным.
     * @param processChart технологическая карта
     * @return HashMap с информацией о рецептурных компонентах
     */
    private Map<Long, IngredientOfRecipeDto> getRecipeCompositionInfo(ProcessChart processChart) {
        Map<Long, IngredientOfRecipeDto> components = new HashMap<>();
        List<RecipeComposition> recipeCompositions = processChart.getRecipeCompositions();
        if (recipeCompositions.isEmpty()) {
            throw new IncorrectDataInDatabaseException(
                    "Для техкарты с ID = " + processChart.getId() + " не заданы рецептурные компоненты");
        }
        StockItem stockItem;
        for (RecipeComposition item : recipeCompositions) {
            // TODO: ? проверять корректность ссылок на п/ф или ингредиент
            if (item.getSemiFinished() != null) {
                components.put(item.getId(), new IngredientOfRecipeDto(
                        item.getSemiFinished().getName(),
                        item.getNetto().doubleValue(),
                        item.getSemiFinished().getId()));
            } else {
                stockItem = item.getIngredient().getStockItem();
                components.put(item.getId(), new IngredientOfRecipeDto(
                        item.getIngredient().getName(),
                        item.getNetto().doubleValue(),
                        stockItem != null && stockItem.getUnitMeasure().getId() == 3));
                // TODO: ссылка на магические единицы измерения
            }
        }
        return components;
    }

    /**
     * Получение списка позиций меню и полуфабрикатов, для которых есть техкарта.
     * Список разбит по группам, для каждой позиции указаны имя и ID техкарты.
     * @return HashMap с информацией о названиях групп (ключ) и списком позиций,
     * которые готовятся на предприятии
     */
    public Map<String, ArrayList<CookItemDto>> getProductionList() {
        Map<String, ArrayList<CookItemDto>> cookItems = new HashMap<>();
        cookItems.put("полуфабрикаты", getListOfSemiFinishedProducts());
        cookItems.putAll(getListOfMenuItemWithProcessChart());
        return cookItems;
    }

    /**
     * Получение списка позиций меню, для которых есть техкарта.
     * Список разбит по группам меню, для каждой позиции указаны имя и ID техкарты.
     * @return HashMap с информацией о названиях групп (ключ) и списком позиций,
     * которые готовятся на предприятии
     */
    private Map<String, ArrayList<CookItemDto>> getListOfMenuItemWithProcessChart() {
        List<MenuItem> menuItems = orderService.getMenuItems();
        Map<Integer, ArrayList<CookItemDto>> positions = new HashMap<>();
        Integer key;
        CookItemDto cookItem;
        for (MenuItem item : menuItems) {
            if (item.getProcessChart() != null) {
                key = item.getMenuGroup().getId();
                cookItem = new CookItemDto(item.getName(), item.getProcessChart().getId());
                if (positions.containsKey(key))
                    positions.get(key).add(cookItem);
                else positions.put(key, new ArrayList<>(Arrays.asList(cookItem)));
            }
        }
        return replaceMenuGroupIdWithName(positions);
    }

    /**
     * Заменяет ID группы меню соответствующим названием группы
     * @param positions HashMap с ID группы меню в качестве ключей
     * @return HashMap с названиями групп меню в качестве ключей
     */
    private Map<String, ArrayList<CookItemDto>> replaceMenuGroupIdWithName(
            Map<Integer, ArrayList<CookItemDto>> positions
    ) {
        List<MenuGroup> menuGroups = orderService.getMenuGroupsList();
        HashMap<Integer, String> groupsName = new HashMap<>();
        for (MenuGroup group : menuGroups) {
            groupsName.put(group.getId(), group.getName());
        }

        Map<String, ArrayList<CookItemDto>> cookItems = new HashMap<>();
        for (var position : positions.entrySet()) {
            cookItems.put(groupsName.get(position.getKey()), position.getValue());
        }
        return cookItems;
    }

    /**
     * Получение списка всех доступных полуфабрикатов с информацией о названии и ID техкарты.
     * @return список с информацией о полуфабрикатах
     */
    private ArrayList<CookItemDto> getListOfSemiFinishedProducts() {
        List<SemiFinished> semiFinishedList = semiFinishedRepo.findAll();
        ArrayList<CookItemDto> cookItems = new ArrayList<>();
        for (SemiFinished product : semiFinishedList) {
            cookItems.add(new CookItemDto(
                    product.getName(), product.getProcessChart().getId()));
        }
        return cookItems;
    }

    // public HashMap<Long, BigDecimal> calcNecessaryIngrForCook(
    //         @NonNull HashMap<Long, Integer> processCharts) {
    //     Optional<ProcessChart> processChart;
    //     for (Long pcId : processCharts.keySet()) {
    //         processChart = processChartRepo.findById(pcId);
    //
    //     }
    //
    //
    //     return null;
    // }
}
