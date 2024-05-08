package com.kirin.outlet.service;

import com.kirin.outlet.model.Ordering;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.dto.OrderDto;
import com.kirin.outlet.model.dto.ShopCartItemDto;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.OrderingRepo;
import com.kirin.outlet.repository.ShoppingCartRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы с заказами
 */
@Validated
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderingRepo orderingRepo;

    private final ShoppingCartRepo shoppingCartRepo;

    private final StockService stockService;

    private final MenuService menuService;

    /**
     * Создание заказа. Транзакция. Фиксирует информацию о заказе (номер оплаченного чека,
     * дату регистрации, пробитые позиции меню и их количество). После успешного создания заказа
     * со склада списываются соответствующие продукты.
     *
     * @param orderData данные о заказе
     * @return ID созданного заказа
     */
    public long createNewOrdering(@Valid OrderDto orderData) {
        checkDataForCreateOrder(orderData);
        try {
            return createOrdering(orderData);
        } catch (Exception e) {
            throw new CannotCreateTransactionException(
                    "Ошибка создания заказа: " + orderData + ". " + e.getMessage(), e);
        }
    }

    /**
     * Транзакция. Создание заказа и списка позиций в заказе, списание продуктов со склада.
     *
     * @param orderData данные для создания заказа
     * @return ID созданного заказа
     */
    @Transactional
    private long createOrdering(OrderDto orderData) {
        Ordering order = orderingRepo.save(new Ordering(orderData.getReceiptId()));
        // System.out.println("Создан заказ: " + order);

        saveShoppingCart(order.getId(), orderData.getShoppingCartItems());

        // TODO: доделать списание продуктов со склада после продажи,
        //  переделать на связь с menuService
        stockService.writeOffProductsFromStock(orderData.getShoppingCartItems());

        return order.getId();
    }

    /**
     * Проверка поступивших данных о новом заказе. ID чека не должен быть зарегистрирован
     * в таблице заказов, должны быть указаны ID существующих позиций меню без повторений.
     *
     * @param orderData данные о новом оплаченном заказе
     */
    private void checkDataForCreateOrder(OrderDto orderData) {
        Optional<Ordering> order = orderingRepo.findByReceiptId(orderData.getReceiptId());
        if (order.isPresent()) {
            throw new IncorrectRequestDataException("checkDataForCreateOrder.orderData.receiptId",
                    "Для чека с ID = " + orderData.getReceiptId()
                            + " уже ранее был создан заказ (ID = " + order.get().getId() + ")");
        }
        Set<Long> menuItemsId = orderData.getShoppingCartItems().stream()
                .mapToLong(ShopCartItemDto::getMenuItemId)
                .boxed().collect(Collectors.toSet());
        if (orderData.getShoppingCartItems().size() > menuItemsId.size())
            throw new IncorrectRequestDataException("checkDataForCreateOrder.orderData.shoppingCartItems",
                    "В корзине заказа есть повторяющиеся позиции меню");
        menuService.checkPresentAllMenuItemsById(menuItemsId);
    }

    /**
     * Сохранение информации о пробитых в заказе позициях.
     *
     * @param orderingId        ID созданного заказа
     * @param shoppingCartItems информация о пробитых позициях меню и их количестве
     */
    private void saveShoppingCart(long orderingId, List<ShopCartItemDto> shoppingCartItems) {
        for (ShopCartItemDto item : shoppingCartItems) {
            shoppingCartRepo.save(
                    new ShoppingCart(orderingId, item.getMenuItemId(), item.getQuantity()));
        }
    }

    /**
     * Получение заказа по его ID.
     *
     * @param id уникальный идентификатор заказа
     * @return информацию о заказе и пробитых позициях
     */
    public Ordering getOrderingById(@Positive long id) {
        Optional<Ordering> order = orderingRepo.findById(id);
        if (order.isEmpty())
            throw new ItemNotFoundException("Заказ с ID = " + id + " не найден");
        return order.get();
    }

    /**
     * Получение списка заказов, созданных в указанную дату. Список может быть пустым.
     * Найденные заказы сортируются по ID в обратном порядке.
     *
     * @param date дата для поиска
     * @return список найденных заказов
     */
    public List<Ordering> getOrderingListByDate(
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Требуется дата в формате 'yyyy-MM-dd'")
            String date
    ) {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IncorrectRequestDataException(
                    "getOrderingListByDate.date", "Некорректный формат даты в параметре запроса");
        }
        if (targetDate.isAfter(LocalDate.now()))
            throw new IncorrectRequestDataException(
                    "getOrderingListByDate.date", "Передана дата после текущей");
        return getOrdersByDate(targetDate);
    }

    /**
     * Получение списка заказов, созданных в текущую дату. Список может быть пустым.
     * Найденные заказы сортируются по ID в обратном порядке.
     *
     * @return список найденных заказов
     */
    public List<Ordering> getOrderingListByCurrDate() {
        return getOrdersByDate(LocalDate.now());
    }

    /**
     * Получение списка заказов, созданных в указанную дату. Список может быть пустым.
     * Найденные заказы сортируются по ID в обратном порядке.
     *
     * @param date дата для поиска
     * @return отсортированный список найденных заказов
     */
    private List<Ordering> getOrdersByDate(LocalDate date) {
        List<Ordering> orderings = orderingRepo.findOrdersByDate(date);
        orderings.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        return orderings;
    }

    /**
     * Отмена заказа по ID. Если заказ с указанным ID существует и не был отменен ранее,
     * фиксируются дата и время отмены заказа и осуществляется расчет и возврат затраченных
     * позиций на склад (транзакция).
     *
     * @param id уникальный идентификатор заказа
     */
    public void cancelOrderingById(@Positive long id) {
        // TODO: отменять только заказы от определенной даты
        Ordering ordering = getOrderingById(id);
        if (ordering.getCancelledAt() != null) // TODO: возвращать объект без изменений
            throw new IncorrectRequestDataException("cancelOrderingById.id",
                    "Заказ с ID = " + id + " уже был отменен " + ordering.getCancelledAt());
        try {
            cancelOrdering(ordering);
        } catch (Exception e) {
            throw new CannotCreateTransactionException(
                    "Не удалось отменить заказ с ID = " + id + ". " + e.getMessage(), e);
        }
    }

    /**
     * Транзакция. Внесение времени отмены заказа и возврат затраченных продуктов на склад.
     *
     * @param ordering объект заказа для отмены
     */
    @Transactional
    private void cancelOrdering(Ordering ordering) {
        ordering.setCancelledAt(Timestamp.valueOf(LocalDateTime.now()));
        orderingRepo.save(ordering);

        // TODO: доделать возврат продуктов на склад после отмены заказа,
        //  переделать на связь с menuService
        stockService.cancelWriteOffProductsFromStock(ordering.getShoppingCarts());
    }

}
