package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.UnitMeasure;
import com.kirin.outlet.model.dto.ShopCartItemDto;
import com.kirin.outlet.model.dto.StockItemDto;
import com.kirin.outlet.model.dto.StockItemQuantityDto;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.MenuItemRepo;
import com.kirin.outlet.repository.StockItemRepo;
import com.kirin.outlet.repository.UnitMeasureRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления складом
 */
@Validated
@Service
@RequiredArgsConstructor
public class StockService {

    private final MenuItemRepo menuItemRepo;

    private final UnitMeasureRepo unitMeasureRepo;

    private final StockItemRepo stockItemRepo;

    /**
     * Принять поставку продуктов. Если ID позиции ссылается на несуществующий
     * ID позиции на складе, или позиция штучная, а данные по количеству включают дробную часть,
     * то данный продукт не принимается на склад, а его ID записывается и отправляется обратно.
     *
     * @param shipment поставляемые продукты (ID позиций на складе и добавляемое количество)
     * @return список ID непринятых позиций, может быть пустым
     */
    public List<Long> acceptIncomingInventoryShipments(
            @NotEmpty List<@Valid StockItemQuantityDto> shipment
    ) {
        List<Long> rejection = new ArrayList<>();
        StockItem stockItem;
        for (StockItemQuantityDto item : shipment) {
            Optional<StockItem> sItem = stockItemRepo.findById(item.getStockItemId());
            if (sItem.isPresent()) {
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
    public StockItem getStockItemById(@Positive long id) {
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
    public UnitMeasure getUnitMeasureById(@Positive int id) {
        Optional<UnitMeasure> unitMeasure = unitMeasureRepo.findById(id);
        if (unitMeasure.isEmpty())
            throw new ItemNotFoundException("Единицы измерения с ID = " + id + " не найдены");
        return unitMeasure.get();
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
    public void writeOffProduct(Long id, Double count) { // TODO: не используется
        StockItem sItem = getStockItemById(id);
        sItem.setQuantity(sItem.getQuantity().subtract(BigDecimal.valueOf(count)));
        stockItemRepo.save(sItem);
        // TODO: рассылка уведомлений о проблемах со складом
        if (sItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            System.err.printf("На складе закончилась позиция с ID = %d (%s)\n",
                    id, sItem.getName());
        } else if (sItem.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
            System.err.println(new StringBuilder("Запасы позиции с ID = ")
                    .append(id)
                    .append(" (")
                    .append(sItem.getName())
                    .append(") меньше 0 (")
                    .append(sItem.getQuantity())
                    .append(")"));
        }
    }

    public void writeOffProductsFromStock(List<ShopCartItemDto> shoppingCartItems) {

        System.out.println(">>>>> Продукты съели!");


    }

    public void cancelWriteOffProductsFromStock(List<ShoppingCart> shoppingCarts) {

        System.out.println(">>>>> Продукты вернули после отмены чека!");


    }

    /**
     * Получение списка штучных позиций на складе, которые не связаны с неудаленными позициями
     * меню. Список отсортирован по имени.
     *
     * @return список найденных позиций на складе
     */
    public List<StockItem> getFreeStockItems() {
        List<StockItem> stockItems = stockItemRepo.findByUnitMeasureIdIs(3); // TODO: ед.измер.
        List<MenuItem> menuItems = menuItemRepo.findByStockItemIdIsNotNullAndDeletedAtIsNull();
        List<Long> menuStockItems = menuItems.stream()
                .mapToLong(item -> item.getStockItemId())
                .boxed().toList();
        return stockItems.stream()
                .filter(item -> !menuStockItems.contains(item.getId()))
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).toList();
    }

    /**
     * Создание позиции на складе с нулевым запасом.
     *
     * @param dto данные об уникальном имени и ID единицы измерения
     * @return созданная позиция на складе с присвоенным ID
     */
    public StockItem createStockItem(@Valid StockItemDto dto) {
        checkUniqueStockItemName(dto.getName());
        getUnitMeasureById(dto.getUnitMeasureId());

        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        return stockItemRepo.save(new StockItem(name, dto.getUnitMeasureId()));
    }

    /**
     * Проверка на отсутствие в репозитории позиции на складе с указанным именем
     * (игнорируя регистр).
     *
     * @param name проверяемое имя
     */
    private void checkUniqueStockItemName(String name) {
        List<StockItem> items = stockItemRepo.findByNameIgnoreCase(name);
        if (items.size() > 0) throw new IncorrectRequestDataException(
                "checkUniqueStockItemName.name", "Имя позиции на складе не уникально");
    }

}
