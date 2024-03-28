package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.Ordering;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.dto.OrderDto;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.repository.MenuGroupRepo;
import com.kirin.outlet.repository.MenuItemRepo;
import com.kirin.outlet.repository.OrderingRepo;
import com.kirin.outlet.repository.ShoppingCartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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
        Ordering order;
        try {
            order = orderingRepo.save(new Ordering(orderData.getReceiptId()));
        } catch (Exception e) {
            throw new IncorrectDataInDatabaseException(
                    "Не создан заказ для чека с ID = " + orderData.getReceiptId(), e);
        }
        System.out.println("Создан заказ:" + order);

        HashMap<Long, Integer> shoppingCartItems = orderData.getShoppingCartItems();
        for (var item : shoppingCartItems.entrySet()) {
            shoppingCartRepo.save(
                    new ShoppingCart(order.getId(), item.getKey(), item.getValue()));
        }

        // TODO: доделать списание продуктов со склада после продажи
        stockService.writeOffProductsFromStock(orderData.getShoppingCartItems());

        return order.getId();
    }
}
