package com.kirin.outlet.service;

import com.kirin.outlet.repository.IngredientRepo;
import com.kirin.outlet.repository.ProcessingMethodRepo;
import com.kirin.outlet.repository.StockItemRepo;
import com.kirin.outlet.repository.UnitMeasureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Сервис для управления складом и списком доступных ингредиентов
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final UnitMeasureRepo unitMeasureRepo;

    private final StockItemRepo stockItemRepo;

    private final ProcessingMethodRepo processingMethodRepo;

    private final IngredientRepo ingredientRepo;

    public void writeOffProductsFromStock(HashMap<Long, Integer> shoppingCartItems) {

        System.out.println(">>>>> Продукты съели!");
        // TODO: доделать списание продуктов со склада после продажи

        // HashMap<Long, BigDecimal> stockItems = new HashMap<>();
        // HashMap<Long, Integer> processCharts = new HashMap<>();
        // Optional<MenuItem> menuItem;
        // StockItem stockItem;
        // ProcessChart processChart;
        //
        //
        // for (OrderDto item : shoppingCartItems) {
        //     menuItem = menuItemRepo.findById(item.getMenuItemId());
        //     if (menuItem.isEmpty())
        //         throw new ItemNotFoundException("Не найдена позиция меню с ID = " + item.getMenuItemId());
        //     stockItem = menuItem.get().getStockItem();
        //     processChart = menuItem.get().getProcessChart();
        //     if (stockItem != null) {
        //         stockItems.put(stockItem.getId(), BigDecimal.valueOf(item.getQuantity()));
        //     } else if (processChart != null) {
        //         processCharts.put(processChart.getId(), item.getQuantity());
        //     } else throw new IncorrectDataInDatabaseException(
        //             "MenuItem с ID=" + item.getMenuItemId() + " не связана со складом");
        // }
        // if (!processCharts.isEmpty()) {
        //     HashMap<Long, BigDecimal> ingredients =
        //             cookingService.calculateNecessaryIngrForCook(processCharts);
        // }
    }
}
