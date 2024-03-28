package com.kirin.outlet.controller;

import com.kirin.outlet.model.dto.CookItemDto;
import com.kirin.outlet.model.dto.ProcessChartDto;
import com.kirin.outlet.service.CookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/cook")
@RequiredArgsConstructor
public class CookingController {

    private final CookingService cookingService;

    @GetMapping
    public ResponseEntity<Map<String, ArrayList<CookItemDto>>> getProductionList() {
        return ResponseEntity.ok(cookingService.getProductionList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessChartDto> getProcessChartInfo(
            @PathVariable("id") Long processChartId
    ) {
        return ResponseEntity.ok(cookingService.getProcessChartInfo(processChartId));
    }
}
