package com.kirin.outlet.controller;

import com.kirin.outlet.model.SemiFinished;
import com.kirin.outlet.model.dto.ProcessChartDto;
import com.kirin.outlet.model.dto.SemiFinishedDto;
import com.kirin.outlet.service.CookingService;
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

@Tag(name = "Cooking", description = "Информация для кухни")
@RestController
@RequestMapping("/cook")
@RequiredArgsConstructor
public class CookingController {

    private final CookingService cookingService;

    @Operation(summary = "Получение информации о техкарте по ID",
            description = "Возвращает информацию о техкарте и рецептурных компонентах, " +
                    "отсортированных по ID")
    @GetMapping("/pc/{id}")
    public ResponseEntity<ProcessChartDto> getProcessChartInfo(
            @Parameter(name = "id", description = "ProcessChart id", example = "1")
            @PathVariable("id") long processChartId
    ) {
        return ResponseEntity.ok(cookingService.getProcessChartInfo(processChartId));
    }

    @Operation(summary = "Получение списка полуфабрикатов",
            description = "Возвращает список полуфабрикатов, отсортированный по имени")
    @GetMapping("/sf")
    public ResponseEntity<List<SemiFinished>> getSemiFinishedList() {
        return ResponseEntity.ok(cookingService.getSemiFinishedProductsList());
    }

    @Operation(summary = "Получение полуфабриката по ID",
            description = "Возвращает информацию о полуфабрикате")
    @GetMapping("/sf/{id}")
    public ResponseEntity<SemiFinished> getSemiFinished(
            @Parameter(name = "id", description = "SemiFinished id", example = "1")
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(cookingService.getSemiFinishedById(id));
    }

    @Operation(summary = "Создание полуфабриката (с техкартой)",
            description = "Возвращает созданный полуфабрикат")
    @PostMapping("/sf")
    public ResponseEntity<SemiFinished> createSemiFinished(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
                    "Данные для создания полуфабриката и связанной техкарты",
                    content = @Content(examples = {@ExampleObject(
                            value = "{\"name\":\"лимонный сок\",\"processChart\":" +
                                    "{\"description\":\"Лимонный фреш.\",\"yield\":100,\"portion\":1}," +
                                    "\"recipeCompositions\":[{" +
                                    "\"netto\":160,\"ingredientId\":3,\"semiFinishedId\":null}]}"
                    )}))
            @RequestBody SemiFinishedDto dto
    ) {
        return ResponseEntity.ok(cookingService.createSemiFinished(dto));
    }

}
