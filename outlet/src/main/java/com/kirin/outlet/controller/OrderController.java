package com.kirin.outlet.controller;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.dto.OrderDto;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.model.exception.OrderTransactionException;
import com.kirin.outlet.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // http://localhost:8080/menu
    @GetMapping()
    public ResponseEntity<List<MenuGroup>> getMenuGroups() {
        List<MenuGroup> groups = orderService.getMenuGroupsList();
        return ResponseEntity.ok().body(groups);
    }

    // http://localhost:8080/menu/group?id=1
    @GetMapping("/group")
    public ResponseEntity<List<MenuItem>> getMenuItemsInGroup(
            @RequestParam("id") Integer groupId
    ) {
        List<MenuItem> items = orderService.getMenuItemsInGroup(groupId);
        if (items.isEmpty()) {
            throw new ItemNotFoundException("Не найдена группа меню с ID = " + groupId);
        }
        return ResponseEntity.ok().body(items);
    }

    // http://localhost:8080/menu/order
    @PostMapping("/order")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderData) {
        if (orderData == null || orderData.getShoppingCartItems() == null
                || orderData.getShoppingCartItems().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            long orderId = orderService.createNewOrdering(orderData);
            return ResponseEntity.ok().body(orderId);
        } catch (Exception e) {
            throw new OrderTransactionException(
                    "Ошибка создания заказа: " + orderData + ". " + e.getMessage(), e);
        }
    }


}





