package com.kirin.outlet.service;

import com.kirin.outlet.model.Ordering;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.dto.OrderDto;
import com.kirin.outlet.model.dto.ShopCartItemDto;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.OrderingRepo;
import com.kirin.outlet.repository.ShoppingCartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для получения информации о позициях меню и формирования заказов
 */
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
    @Transactional
    public long createNewOrdering(OrderDto orderData) {
        Ordering order = createOrdering(orderData.getReceiptId());
        System.out.println("Создан заказ: " + order);

        saveShoppingCart(order.getId(), orderData.getShoppingCartItems());

        // TODO: доделать списание продуктов со склада после продажи
        stockService.writeOffProductsFromStock(orderData.getShoppingCartItems());

        return order.getId();
    }

    /**
     * Проверка поступивших данных о новом заказе. Данные не должны быть пустыми,
     * ID чека не должен быть зарегистрирован в таблице заказов, должны быть указаны
     * ID существующих позиций меню, количество каждой пробитой позиции в чеке не может
     * быть меньше 1.
     *
     * @param orderData данные о новом оплаченном заказе
     */
    public void checkDataForCreateOrder(OrderDto orderData) {
        if (orderData == null || orderData.getShoppingCartItems() == null
                || orderData.getShoppingCartItems().isEmpty()) {
            throw new IncorrectRequestDataException(
                    "Данные для создания заказа не полные или отсутствуют");
        }

        Optional<Ordering> order = orderingRepo.findByReceiptId(orderData.getReceiptId());
        if (order.isPresent()) {
            throw new IncorrectRequestDataException("Для чека с ID = " + orderData.getReceiptId()
                    + " уже ранее был создан заказ (ID = " + order.get().getId() + ")");
        }

        for (ShopCartItemDto item : orderData.getShoppingCartItems()) {
            menuService.getMenuItemById(item.getMenuItemId());
            if (item.getQuantity() <= 0)
                throw new IncorrectRequestDataException(
                        "Недопустимое количество позиций меню с ID = " + item.getMenuItemId()
                                + " в заказе (" + item.getQuantity() + " шт.)");
        }
    }

    /**
     * Создание нового заказа с указанным ID чека.
     *
     * @param receiptId ID чека
     * @return объект созданного заказа с присвоенным ID
     */
    private Ordering createOrdering(Long receiptId) {
        try {
            return orderingRepo.save(new Ordering(receiptId));
        } catch (Exception e) {
            throw new IncorrectDataInDatabaseException(
                    "Не создан заказ для чека с ID = " + receiptId, e);
        }
    }

    /**
     * Сохранение информации о пробитых в заказе позициях.
     *
     * @param orderingId        ID созданного заказа
     * @param shoppingCartItems информация о пробитых позициях меню и их количестве
     */
    private void saveShoppingCart(Long orderingId, List<ShopCartItemDto> shoppingCartItems) {
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
    public Ordering getOrderingById(Long id) {
        Optional<Ordering> order = orderingRepo.findById(id);
        if (order.isEmpty())
            throw new ItemNotFoundException("Заказ с ID = " + id + " не найден");
        List<ShoppingCart> items = order.get().getShoppingCarts();
        if (items.isEmpty())
            throw new IncorrectDataInDatabaseException(
                    "Для заказа с ID = " + id + " нет списка пробитых позиций");
        return order.get();
    }

    /**
     * Получение списка заказов, созданных в указанную дату. Список может быть пустым.
     * Найденные заказы сортируются по ID в обратном порядке.
     *
     * @param date дата для поиска
     * @return список найденных заказов
     */
    public List<Ordering> getOrderingListByDate(String date) {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IncorrectRequestDataException("Некорректный формат даты в параметре запроса");
        }
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
        if (date.isAfter(LocalDate.now()))
            throw new IncorrectRequestDataException("Передана дата после текущей");
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
    public void cancelOrderingById(Long id) {
        Optional<Ordering> order = orderingRepo.findById(id);
        if (order.isEmpty())
            throw new ItemNotFoundException("Заказ с ID = " + id + " не найден");
        Ordering ordering = order.get();
        if (ordering.getCancelledAt() != null)
            throw new IncorrectRequestDataException(
                    "Заказ с ID = " + id + " уже был отменен " + ordering.getCancelledAt());
        cancelOrdering(ordering);
        // TODO: обработка ошибок транзакции
    }

    /**
     * Внесение времени отмены заказа и возврат затраченных продуктов на склад.
     *
     * @param ordering объект заказа для отмены
     */
    @Transactional
    private void cancelOrdering(Ordering ordering) {
        ordering.setCancelledAt(Timestamp.valueOf(LocalDateTime.now()));
        orderingRepo.save(ordering);

        // TODO: доделать возврат продуктов на склад после отмены заказа
        stockService.cancelWriteOffProductsFromStock(ordering.getShoppingCarts());
    }

}
