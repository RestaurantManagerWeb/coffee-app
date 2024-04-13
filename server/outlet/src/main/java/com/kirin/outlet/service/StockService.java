package com.kirin.outlet.service;

import com.kirin.outlet.model.Ingredient;
import com.kirin.outlet.model.ProcessingMethod;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.UnitMeasure;
import com.kirin.outlet.model.dto.ShopCartItemDto;
import com.kirin.outlet.model.dto.StockItemDto;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.IngredientRepo;
import com.kirin.outlet.repository.ProcessingMethodRepo;
import com.kirin.outlet.repository.StockItemRepo;
import com.kirin.outlet.repository.UnitMeasureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
     * Принять поставку продуктов со склада. Если ID позиции ссылается на несуществующий
     * ID позиции на складе, или передано отрицательное либо нулевое количество,
     * или позиция штучная, а данные по количеству включают дробную часть,
     * то данный продукт не принимается на склад, а его ID записывается и отправляется обратно.
     *
     * @param shipment поставляемые продукты (ID продукта : количество в г, мл или шт)
     * @return список непринятых позиций, может быть пустым
     */
    public List<Long> acceptIncomingInventoryShipments(List<StockItemDto> shipment) {
        List<Long> rejection = new ArrayList<>();
        StockItem stockItem;
        for (StockItemDto item : shipment) {
            Optional<StockItem> sItem = stockItemRepo.findById(item.getStockItemId());
            if (sItem.isPresent() && item.getQuantity() > 0) {
                stockItem = sItem.get();
                // TODO: ссылка на магические единицы измерения
                if (stockItem.getUnitMeasure().getId() == 3 && item.getQuantity() % 1 != 0) {
                    rejection.add(item.getStockItemId());
                } else {
                    stockItem.setQuantity(
                            stockItem.getQuantity().add(BigDecimal.valueOf(item.getQuantity())));
                    stockItemRepo.save(stockItem);
                }
            } else rejection.add(item.getStockItemId());
        }
        return rejection;
    }

    /**
     * Получение позиции на складе по ID.
     *
     * @param id уникальный идентификатор позиции на складе
     * @return найденную позицию на складе
     */
    public StockItem getStockItemById(Long id) {
        Optional<StockItem> stockItem = stockItemRepo.findById(id);
        if (stockItem.isEmpty())
            throw new ItemNotFoundException("Позиция на складе с ID = " + id + " не найдена");
        return stockItem.get();
    }

    /**
     * Получение информации о единице измерения по ID.
     *
     * @param id уникальный идентификатор единицы измерения
     * @return информацию о найденной единице измерения
     */
    public UnitMeasure getUnitMeasureById(Integer id) {
        Optional<UnitMeasure> unitMeasure = unitMeasureRepo.findById(id);
        if (unitMeasure.isEmpty())
            throw new ItemNotFoundException("Единицы измерения с ID = " + id + " не найдены");
        return unitMeasure.get();
    }

    /**
     * Получение информации о методе обработки по ID.
     *
     * @param id уникальный идентификатор метода обработки продуктов
     * @return информацию о найденном методе обработки
     */
    public ProcessingMethod getProcessingMethodById(Integer id) {
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
    public Ingredient getIngredientById(Long id) {
        Optional<Ingredient> ingredient = ingredientRepo.findById(id);
        if (ingredient.isEmpty())
            throw new ItemNotFoundException("Ингредиент с ID = " + id + " не найден");
        return ingredient.get();
    }

    /**
     * Получение списка всех позиций на складе, отсортированного по имени.
     *
     * @return отсортированный список позиций на складе
     */
    public List<StockItem> getStockItemsList() {
        List<StockItem> stockItems = stockItemRepo.findAll();
        stockItems.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return stockItems;
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

    /**
     * Получение списка всех единиц измерения.
     *
     * @return список единиц измерения
     */
    public List<UnitMeasure> getUnitsMeasureList() {
        return unitMeasureRepo.findAll();
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

    public void writeOffProductsFromStock(List<ShopCartItemDto> shoppingCartItems) {

        System.out.println(">>>>> Продукты съели!");


    }

    public void cancelWriteOffProductsFromStock(List<ShoppingCart> shoppingCarts) {

        System.out.println(">>>>> Продукты вернули после отмены чека!");


    }

}
