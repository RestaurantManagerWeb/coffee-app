package com.kirin.outlet.service;

import com.kirin.outlet.model.ProcessChart;
import com.kirin.outlet.model.RecipeComposition;
import com.kirin.outlet.model.SemiFinished;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.dto.ProcessChartDto;
import com.kirin.outlet.model.dto.RecipeCompositionDto;
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

    /**
     * Получение информации о техкарте и требуемых ингредиентах.
     *
     * @param processChartId ID техкарты
     * @return объект с данными о техкарте и списком ингредиентов
     */
    public ProcessChartDto getProcessChartInfo(Long processChartId) {
        Optional<ProcessChart> processChart = processChartRepo.findById(processChartId);
        if (processChart.isEmpty())
            throw new ItemNotFoundException("Техкарта с ID = " + processChartId + " не найдена");

        List<RecipeComposition> recipeCompositions = processChart.get().getRecipeCompositions();
        if (recipeCompositions.isEmpty())
            throw new IncorrectDataInDatabaseException(
                    "Для техкарты с ID = " + processChart.get().getId()
                            + " не заданы рецептурные компоненты");
        ProcessChartDto pcDto = new ProcessChartDto();
        pcDto.setProcessChart(processChart.get());
        pcDto.setComponents(getRecipeCompositionInfo(recipeCompositions));
        return pcDto;
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
            checkRecipeComposition(item);
            if (item.getSemiFinished() != null) {
                components.add(new RecipeCompositionDto(item, item.getSemiFinished().getName()));
            } else {
                stockItem = item.getIngredient().getStockItem();
                components.add(new RecipeCompositionDto(item, item.getIngredient().getName(),
                        stockItem != null && stockItem.getUnitMeasure().getId() == 3));
                // TODO: ссылка на магические единицы измерения
            }
        }
        Collections.sort(components);
        return components;
    }

    /**
     * Проверка корректности связей рецептурного компонента, записанного в базе данных,
     * с сущностями ингредиента и полуфабриката.
     *
     * @param rc проверяемый рецептурный компонент
     */
    private void checkRecipeComposition(RecipeComposition rc) {
        if (rc.getSemiFinished() != null && rc.getIngredient() != null)
            throw new IncorrectDataInDatabaseException(
                    "У рецептурного компонента с ID = " + rc.getId()
                            + " есть связь и с ингредиентом, и с полуфабрикатом");
        if (rc.getSemiFinished() == null && rc.getIngredient() == null)
            throw new IncorrectDataInDatabaseException(
                    "У рецептурного компонента с ID = " + rc.getId()
                            + " нет связи ни с ингредиентом, ни с полуфабрикатом");
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

    public void deleteProcessChart(ProcessChart processChart) {
        // TODO: удаление техкарты
    }
}
