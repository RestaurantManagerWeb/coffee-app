package com.kirin.outlet.service;

import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.IngredientRepo;
import com.kirin.outlet.repository.ProcessingMethodRepo;
import com.kirin.outlet.repository.StockItemRepo;
import com.kirin.outlet.repository.UnitMeasureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    /**
     * Принять поставку продуктов со склада. Если ключ в словаре ссылается на несуществующий
     * ID позиции на складе или позиция штучная, а данные по количеству включают дробную часть,
     * то данный продукт не принимается на склад, а его ID записывается и отправляется обратно.
     *
     * @param shipment поставляемые продукты (ID продукта : количество в г, мл или шт)
     * @return список непринятых позиций, может быть пустым
     */
    // acceptIncomingShipmentsOfGoods
    public ArrayList<Long> acceptIncomingInventoryShipments(Map<Long, Double> shipment) {
        ArrayList<Long> rejection = new ArrayList<>();
        StockItem stockItem;
        for (var item : shipment.entrySet()) {
            // TODO: проверка на получение отрицательного количества
            Optional<StockItem> sItem = stockItemRepo.findById(item.getKey());
            if (sItem.isPresent()) {
                stockItem = sItem.get();
                // TODO: ссылка на магические единицы измерения
                if (stockItem.getUnitMeasure().getId() == 3 && item.getValue() % 1 != 0) {
                    rejection.add(item.getKey());
                } else {
                    stockItem.setQuantity(
                            stockItem.getQuantity().add(BigDecimal.valueOf(item.getValue())));
                    stockItemRepo.save(stockItem);
                }
            } else {
                rejection.add(item.getKey());
            }
        }
        return rejection;
    }

    /**
     * Списание указанного количества продукта (товара) со склада. Если оставшиеся запасы
     * продукта (товара) равны 0 или меньше 0, будет отправлено уведомление. Если продукт
     * (товар) с указанным ID не будет найден, то будет выбрашено исключение
     * ItemNotFoundException.
     *
     * @param id    идентификатор продукта (товара) на складе
     * @param count количество для списания
     */
    public void writeOffProduct(Long id, Double count) {
        Optional<StockItem> sItem = stockItemRepo.findById(id);
        if (sItem.isEmpty()) {
            throw new ItemNotFoundException(
                    "Не найдена позиция на складе с ID = " + id + ". Нельзя списать продукт");
        }
        sItem.get().setQuantity(
                sItem.get().getQuantity().subtract(BigDecimal.valueOf(count)));
        stockItemRepo.save(sItem.get());
        // TODO: рассылка уведомлений о проблемах со складом
        if (sItem.get().getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            System.err.printf("На складе закончилась позиция с ID = %d (%s)\n",
                    id, sItem.get().getName());
        } else if (sItem.get().getQuantity().compareTo(BigDecimal.ZERO) < 0) {
            System.err.println(new StringBuilder("Запасы позиции с ID = ")
                    .append(id)
                    .append(" (")
                    .append(sItem.get().getName())
                    .append(") меньше 0 (")
                    .append(sItem.get().getQuantity())
                    .append(")"));
        }

    }

    public void writeOffProductsFromStock(HashMap<Long, Integer> shoppingCartItems) {

        System.out.println(">>>>> Продукты съели!");


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
