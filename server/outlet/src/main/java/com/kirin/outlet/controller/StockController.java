package com.kirin.outlet.controller;

import com.kirin.outlet.model.dto.StockItemDto;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PutMapping("/shipment")
    public ResponseEntity<List<Long>> acceptNewShipment(
            @RequestBody List<StockItemDto> shipment
    ) {
        if (shipment.isEmpty())
            throw new IncorrectRequestDataException("Передан пустой список поставок");
        return ResponseEntity.ok(stockService.acceptIncomingInventoryShipments(shipment));
    }

}
