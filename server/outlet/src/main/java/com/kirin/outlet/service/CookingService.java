package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.ProcessChart;
import com.kirin.outlet.model.RecipeComposition;
import com.kirin.outlet.model.SemiFinished;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.dto.CookGroupDto;
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

import java.util.*;


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
     * Получение информации о рецептурных компонентах, требующихся для приготовления по
     * техкарте с указанным ID: ID компонента, название, масса нетто, для п/ф - ID техкарты,
     * для ингредиента - является ли он штучным.
     * @param processChart технологическая карта
     * @return список с информацией о рецептурных компонентах
     */
    private List<IngredientOfRecipeDto> getRecipeCompositionInfo(ProcessChart processChart) {
        List<IngredientOfRecipeDto> components = new ArrayList<>();
        List<RecipeComposition> recipeCompositions = processChart.getRecipeCompositions();
        if (recipeCompositions.isEmpty()) {
            throw new IncorrectDataInDatabaseException(
                    "Для техкарты с ID = " + processChart.getId() + " не заданы рецептурные компоненты");
        }
        StockItem stockItem;
        for (RecipeComposition item : recipeCompositions) {
            // TODO: ? проверять корректность ссылок на п/ф или ингредиент
            if (item.getSemiFinished() != null) {
                components.add(new IngredientOfRecipeDto(
                        item.getId(),
                        item.getSemiFinished().getName(),
                        item.getNetto().doubleValue(),
                        item.getSemiFinished().getId()));
            } else {
                stockItem = item.getIngredient().getStockItem();
                components.add(new IngredientOfRecipeDto(
                        item.getId(),
                        item.getIngredient().getName(),
                        item.getNetto().doubleValue(),
                        stockItem != null && stockItem.getUnitMeasure().getId() == 3));
                // TODO: ссылка на магические единицы измерения
            }
        }
        Collections.sort(components);
        return components;
    }

    /**
     * Получение списка позиций меню и полуфабрикатов, для которых есть техкарта.
     * Список разбит по группам, для каждой позиции указаны имя и ID техкарты.
     * @return список с информацией о позициях, которые готовятся на предприятии
     */
    public List<CookGroupDto> getProductionList() {
        List<CookGroupDto> cookItems = new ArrayList<>();
        // TODO: магические полуфабрикаты
        cookItems.add(new CookGroupDto(
                "полуфабрикаты", getListOfSemiFinishedProducts()));
        cookItems.addAll(getCookGroupsWithMenuItems());
        return cookItems;
    }

    /**
     * Получение списка позиций меню, для которых есть техкарта.
     * Список разбит по группам меню, для каждой позиции указаны имя и ID техкарты.
     * @return коллекция с информацией о названиях групп и списком позиций,
     * которые готовятся на предприятии
     */
    private Collection<CookGroupDto> getCookGroupsWithMenuItems() {
        List<MenuItem> menuItems = orderService.getMenuItems();
        Map<Integer, CookGroupDto> positions = new HashMap<>();
        Integer key;
        CookItemDto cookItem;
        for (MenuItem item : menuItems) {
            if (item.getProcessChart() != null) {
                key = item.getMenuGroup().getId();
                cookItem = new CookItemDto(item.getName(), item.getProcessChart().getId());
                if (positions.containsKey(key))
                    positions.get(key).addItem(cookItem);
                else positions.put(key, new CookGroupDto(cookItem));
            }
        }
        setNamesCookGroups(positions);
        return positions.values();
    }

    /**
     * Устанавливает название группы по ID группы.
     * @param positions HashMap с ID группы меню в качестве ключей
     */
    private void setNamesCookGroups(Map<Integer, CookGroupDto> positions) {
        List<MenuGroup> menuGroups = orderService.getMenuGroupsList();

        HashMap<Integer, String> groupsName = new HashMap<>();
        for (MenuGroup group : menuGroups) {
            groupsName.put(group.getId(), group.getName());
        }

        for (var cookGroup : positions.entrySet()) {
            cookGroup.getValue().setName(groupsName.get(cookGroup.getKey()));
        }
    }

    /**
     * Получение списка всех доступных полуфабрикатов с информацией о названии и ID техкарты.
     * @return список с информацией о полуфабрикатах
     */
    private List<CookItemDto> getListOfSemiFinishedProducts() {
        List<SemiFinished> semiFinishedList = semiFinishedRepo.findAll();
        List<CookItemDto> cookItems = new LinkedList<>();
        for (SemiFinished product : semiFinishedList) {
            cookItems.add(new CookItemDto(
                    product.getName(), product.getProcessChart().getId()));
        }
        return cookItems;
    }

}
