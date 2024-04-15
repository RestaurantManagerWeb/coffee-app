package com.kirin.outlet.controller;

import com.kirin.outlet.model.Ingredient;
import com.kirin.outlet.model.ProcessingMethod;
import com.kirin.outlet.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ingredients", description = "Работа с ингредиентами")
@RestController
@RequestMapping("/ingr")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @Operation(summary = "Получение списка ингредиентов",
            description = "Возвращает список ингредиентов, отсортированный по имени")
    @GetMapping()
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity.ok().body(ingredientService.getIngredientsList());
    }

    @Operation(summary = "Получение ингредиента по ID",
            description = "Возвращает информацию об ингредиенте")
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredient(
            @Parameter(name = "id", description = "Ingredient id", example = "1")
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @Operation(summary = "Получение списка методов обработки",
            description = "Возвращает список методов обработки ингредиентов")
    @GetMapping("/pm")
    public ResponseEntity<List<ProcessingMethod>> getProcessingMethods() {
        return ResponseEntity.ok().body(ingredientService.getProcessingMethodsList());
    }

    @Operation(summary = "Получение метода обработки по ID",
            description = "Возвращает информацию о методе обработки ингредиента")
    @GetMapping("/pm/{id}")
    public ResponseEntity<ProcessingMethod> getProcessingMethod(
            @Parameter(name = "id", description = "ProcessingMethod id", example = "1")
            @PathVariable("id") int id
    ) {
        return ResponseEntity.ok(ingredientService.getProcessingMethodById(id));
    }
}
