package com.kirin.outlet.controller;

import com.kirin.outlet.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/outlet/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PutMapping("/shipment")
    public ResponseEntity<ArrayList<Long>> acceptNewShipment(
            @RequestBody Map<Long, Double> shipment
    ) {
        ArrayList<Long> rejection = stockService.acceptIncomingInventoryShipments(shipment);
        return ResponseEntity.ok(rejection);
    }

}
