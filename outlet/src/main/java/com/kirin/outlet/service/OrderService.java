package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.Ordering;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.ShoppingCartPK;
import com.kirin.outlet.model.dto.OrderDto;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.OrderTransactionException;
import com.kirin.outlet.repository.MenuGroupRepo;
import com.kirin.outlet.repository.MenuItemRepo;
import com.kirin.outlet.repository.OrderingRepo;
import com.kirin.outlet.repository.ShoppingCartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для получения информации о позициях меню и формирования заказов
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuItemRepo menuItemRepo;

    private final MenuGroupRepo menuGroupRepo;

    private final OrderingRepo orderingRepo;

    private final ShoppingCartRepo shoppingCartRepo;

    private final StockService stockService;

    /**
     * Получения списка групп меню.
     * @return список групп меню
     */
    public List<MenuGroup> getMenuGroupsList() {
        return menuGroupRepo.findAll();
    }

    /**
     * Получение списка позиций меню в указанной группе по ID группы.
     * @param groupId ID группы меню
     * @return список позиций меню в группе, может быть пустым, если группа не найдена
     */
    public List<MenuItem> getMenuItemsInGroup(Integer groupId) {
        // TODO: сделать двунаправл связь с MenuGroup
        return menuItemRepo.findByMenuGroupId(groupId);
    }

    /**
     * Получение списка всех позиций меню.
     * @return список позиций меню
     */
    public List<MenuItem> getMenuItems() {
        return menuItemRepo.findAll();
    }

    /**
     * Создание заказа. Транзакция. Фиксирует информацию о заказе (номер оплаченного чека,
     * дата регистрации, пробитые позиции меню и их количество). После успешного создания заказа
     * со склада списываются соответствующие продукты.
     * @param orderData данные о заказе
     * @return ID созданного заказа
     */
    @Transactional
    public long createNewOrdering(OrderDto orderData) {
        Ordering order = createOrdering(orderData.getReceiptId());
        System.out.println("Создан заказ:" + order);

        saveShoppingCart(order.getId(), orderData.getShoppingCartItems());

        // TODO: доделать списание продуктов со склада после продажи
        stockService.writeOffProductsFromStock(orderData.getShoppingCartItems());

        return order.getId();
    }

    /**
     * Проверка поступивших данных о новом заказе. Данные не должны быть пустыми,
     * ID чека не должен быть зарегистрирован в таблице заказов, количество пробитых
     * позиций в чеке не может быть меньше 1.
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

        for (var item : orderData.getShoppingCartItems().entrySet()) {
            if (item.getValue() <= 0)
                throw new IncorrectRequestDataException(
                        "Недопустимое количество позиций меню с ID = " + item.getKey()
                                + " в заказе (" + item.getValue() + " шт.)");
        }
    }

    /**
     * Создание нового заказа с указанным ID чека.
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
     * @param orderingId ID созданного заказа
     * @param shoppingCartItems информация о пробитых позициях меню и их количестве
     */
    private void saveShoppingCart(Long orderingId, HashMap<Long, Integer> shoppingCartItems) {
        for (var item : shoppingCartItems.entrySet()) {
            shoppingCartRepo.save(
                    new ShoppingCart(orderingId, item.getKey(), item.getValue()));
        }
    }
}
