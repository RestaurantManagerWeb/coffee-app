package com.kirin.outlet.controller;

import com.kirin.outlet.model.Ordering;
import com.kirin.outlet.model.dto.OrderDto;
import com.kirin.outlet.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Orders", description = "Работа с заказами")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Создание нового заказа",
            description = "Транзакция. Возвращает ID созданного заказа")
    @PostMapping()
    public ResponseEntity<Long> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID чека и пробитые позиции",
                    content = @Content(examples = {@ExampleObject(
                            value = "{\"receiptId\":8,\"shoppingCartItems\":[" +
                                    "{\"menuItemId\":1,\"quantity\":2}," +
                                    "{\"menuItemId\":2,\"quantity\":1}," +
                                    "{\"menuItemId\":3,\"quantity\":1}]}"
                    )}))
            @RequestBody OrderDto orderData
    ) {
        return ResponseEntity.ok().body(orderService.createNewOrdering(orderData));
    }

    @Operation(summary = "Получение информации о заказе по ID",
            description = "Возвращает данные о найденном заказе и списке пробитых позиций")
    @GetMapping("/{id}")
    public ResponseEntity<Ordering> getOrdering(
            @Parameter(name = "id", description = "Ordering id", example = "1")
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(orderService.getOrderingById(id));
    }

    @Operation(summary = "Отмена заказа по ID")
    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelOrdering(
            @Parameter(name = "id", description = "Ordering id", example = "1")
            @PathVariable("id") long id
    ) {
        orderService.cancelOrderingById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Получение списка созданных заказов в указанную дату",
            description = "Возвращает данные о найденных заказах и списке пробитых позиций. " +
                    "Список отсортирован по ID заказа в обратном порядке")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Ordering>> getOrdersByDate(
            @Parameter(name = "date", description = "Date (format: yyyy-MM-dd)", example = "2024-01-15")
            @PathVariable("date") String date
    ) {
        return ResponseEntity.ok(orderService.getOrderingListByDate(date));
    }

    @Operation(summary = "Получение списка созданных заказов в текущий день",
            description = "Возвращает данные о найденных заказах и списке пробитых позиций. " +
                    "Список отсортирован по ID заказа в обратном порядке")
    @GetMapping("/date/current")
    public ResponseEntity<List<Ordering>> getOrdersByCurrDate() {
        return ResponseEntity.ok(orderService.getOrderingListByCurrDate());
    }

}





