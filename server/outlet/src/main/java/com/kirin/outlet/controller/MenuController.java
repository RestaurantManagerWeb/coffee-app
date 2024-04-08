package com.kirin.outlet.controller;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "Menu", description = "Получение информации по меню")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "Получение списка групп меню",
            description = "Возвращает список групп, отсортированный по имени группы")
    @GetMapping()
    public ResponseEntity<List<MenuGroup>> getMenuGroups() {
        return ResponseEntity.ok().body(menuService.getMenuGroupsList());
    }

    @Operation(summary = "Получение списка позиций меню в группе по ID группы",
            description = "Возвращает список позиций меню, отсортированный по имени")
    @GetMapping("/group/{id}")
    public ResponseEntity<List<MenuItem>> getMenuItemsInGroup(
            @Parameter(name = "id", description = "MenuGroup id", example = "1")
            @PathVariable("id") Integer groupId
    ) {
        return ResponseEntity.ok().body(menuService.getMenuItemsInGroup(groupId));
    }

    @Operation(summary = "Получение позиции меню по ID",
            description = "Возвращает найденную позицию меню")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found - The MenuItem was not found")
    })
    @GetMapping("/item/{id}")
    public ResponseEntity<MenuItem> getMenuItem(
            @Parameter(name = "id", description = "MenuItem id", example = "1")
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(menuService.getMenuItemById(id));
    }

    @Operation(summary = "Получение группы меню по ID",
            description = "Возвращает найденную группу меню")
    @GetMapping("/group/info/{id}")
    public ResponseEntity<MenuGroup> getMenuGroup(
            @Parameter(name = "id", description = "MenuGroup id", example = "1")
            @PathVariable("id") Integer id
    ) {
        return ResponseEntity.ok(menuService.getMenuGroupById(id));
    }

    @Operation(summary = "Создание новой группы",
            description = "Возвращает созданный в базе данных объект группы меню")
    @PostMapping("/group/new")
    public ResponseEntity<MenuGroup> createMenuGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Группа меню с данными об имени. Требования: от 3 до 30 символов, " +
                            "русские и латинские, слова разделены через пробел или дефис.")
            @RequestBody MenuGroup menuGroup
    ) {
        return ResponseEntity.ok(menuService.createMenuGroup(menuGroup));
    }

}