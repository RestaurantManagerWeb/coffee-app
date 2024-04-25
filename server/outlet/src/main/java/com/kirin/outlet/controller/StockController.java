package com.kirin.outlet.controller;

import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.UnitMeasure;
import com.kirin.outlet.model.dto.StockItemDto;
import com.kirin.outlet.model.dto.StockItemQuantityDto;
import com.kirin.outlet.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Stock", description = "Работа со складом")
@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "Получение списка позиций на складе",
            description = "Возвращает список позиций на складе, отсортированный по имени")
    @GetMapping()
    public ResponseEntity<List<StockItem>> getStockItems() {
        return ResponseEntity.ok().body(stockService.getStockItemsList());
    }

    @Operation(summary = "Создание позиции на складе",
            description = "Возвращает созданную позицию на складе")
    @PostMapping
    public ResponseEntity<StockItem> createStockItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
                    "Данные об имени и единице измерения для создания новой позиции на складе",
                    content = @Content(examples = {@ExampleObject(
                            value = "{\"name\":\"сыр творожный сливочный\",\"unitMeasureId\":1}"
                    )}))
            @RequestBody StockItemDto dto
    ) {
        return ResponseEntity.ok(stockService.createStockItem(dto));
    }

    @Operation(summary = "Принятие поставки",
            description = "Возвращает список ID непринятых позиций")
    @PostMapping("/shipment")
    public ResponseEntity<List<Long>> acceptNewShipment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Список с данными об ID позиции на складе и добавляемом количестве",
                    content = @Content(examples = {@ExampleObject(
                            value = "[{\"stockItemId\":1,\"quantity\":49}," +
                                    "{\"stockItemId\":3,\"quantity\":215.5}," +
                                    "{\"stockItemId\":6,\"quantity\":12}]"
                    )}))
            @RequestBody List<StockItemQuantityDto> shipment
    ) {
        return ResponseEntity.ok(stockService.acceptIncomingInventoryShipments(shipment));
    }

    @Operation(summary = "Получение позиции на складе по ID",
            description = "Возвращает найденную позицию на складе")
    @GetMapping("/{id}")
    public ResponseEntity<StockItem> getStockItem(
            @Parameter(name = "id", description = "StockItem id", example = "1")
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(stockService.getStockItemById(id));
    }

    @Operation(summary = "Получение списка единиц измерений",
            description = "Возвращает список единиц измерений")
    @GetMapping("/um")
    public ResponseEntity<List<UnitMeasure>> getUnitsMeasure() {
        return ResponseEntity.ok().body(stockService.getUnitsMeasureList());
    }

    @Operation(summary = "Получение единицы измерения по ID",
            description = "Возвращает информацию о единице измерения позиции на складе")
    @GetMapping("/um/{id}")
    public ResponseEntity<UnitMeasure> getUnitMeasure(
            @Parameter(name = "id", description = "UnitMeasure id", example = "1")
            @PathVariable("id") int id
    ) {
        return ResponseEntity.ok(stockService.getUnitMeasureById(id));
    }

    @Operation(summary = "Получение списка несвязанных штучных позиций на складе",
            description = "Возвращает список штучных позиций на складе, которые не связаны " +
                    "с неудаленными позициями меню, отсортированный по имени")
    @GetMapping("/retail/free")
    public ResponseEntity<List<StockItem>> getFreeStockItems() {
        return ResponseEntity.ok(stockService.getFreeStockItems());
    }

}
