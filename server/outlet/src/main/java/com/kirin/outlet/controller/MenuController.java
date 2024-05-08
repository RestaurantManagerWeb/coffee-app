package com.kirin.outlet.controller;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.dto.CookGroupDto;
import com.kirin.outlet.model.dto.MenuGroupDto;
import com.kirin.outlet.model.dto.MenuItemPCDto;
import com.kirin.outlet.model.dto.MenuItemStockDto;
import com.kirin.outlet.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Operation(summary = "Получение списка неудаленных позиций меню",
            description = "Возвращает список неудаленных позиций меню")
    @GetMapping()
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        return ResponseEntity.ok().body(menuService.getMenuItems());
    }

    @Operation(summary = "Получение списка групп меню",
            description = "Возвращает список неудаленных групп, отсортированный по имени группы")
    @GetMapping("/group")
    public ResponseEntity<List<MenuGroup>> getMenuGroups() {
        return ResponseEntity.ok().body(menuService.getMenuGroupsList());
    }

    @Operation(summary = "Получение списка позиций меню в группе по ID группы",
            description = "Возвращает список неудаленных позиций меню, отсортированный по имени")
    @GetMapping("/group/{id}")
    public ResponseEntity<List<MenuItem>> getMenuItemsInGroup(
            @Parameter(name = "id", description = "MenuGroup id", example = "1")
            @PathVariable("id") int groupId
    ) {
        return ResponseEntity.ok().body(menuService.getMenuItemsInGroup(groupId));
    }

    @Operation(summary = "Получение неудаленной позиции меню по ID",
            description = "Возвращает найденную неудаленную позицию меню")
    @GetMapping("/item/{id}")
    public ResponseEntity<MenuItem> getMenuItem(
            @Parameter(name = "id", description = "MenuItem id", example = "1")
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(menuService.getMenuItemById(id));
    }

    @Operation(summary = "Получение позиции меню по ID",
            description = "Возвращает найденную позицию меню, в том числе удаленную")
    @GetMapping("/item/withdel/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(
            @Parameter(name = "id", description = "MenuItem id", example = "1")
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(menuService.getMenuItemWithDeletedById(id));
    }

    @Operation(summary = "Получение группы меню по ID",
            description = "Возвращает найденную неудаленную группу меню")
    @GetMapping("/group/info/{id}")
    public ResponseEntity<MenuGroup> getMenuGroup(
            @Parameter(name = "id", description = "MenuGroup id", example = "1")
            @PathVariable("id") int id
    ) {
        return ResponseEntity.ok(menuService.getMenuGroupById(id));
    }

    @Operation(summary = "Создание новой группы",
            description = "Возвращает созданный в базе данных объект группы меню")
    @PostMapping("/group")
    public ResponseEntity<MenuGroup> createMenuGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные об имени группы. Требования: от 3 до 30 символов, " +
                            "русские и латинские, слова разделены через пробел или дефис",
                    content = @Content(examples = {@ExampleObject(
                            value = "{\"name\":\"кофе\"}"
                    )}))
            @RequestBody MenuGroupDto menuGroupDto
    ) {
        return ResponseEntity.ok(menuService.createMenuGroup(menuGroupDto));
    }

    @Operation(summary = "Создание позиции меню (склад)",
            description = "Возвращает созданную позицию меню")
    @PostMapping("/item/stock")
    public ResponseEntity<MenuItem> createMenuItemWithStockItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
                    "Данные для создания позиции меню, связанной со штучной позицией на складе",
                    content = @Content(examples = {
                            @ExampleObject(name = "Пример 1", summary = "Вода н/г",
                                    value = "{\"name\":\"вода н/г 0.5 л, пластик\",\"price\":100," +
                                            "\"vat\":null,\"menuGroupId\":2,\"stockItemId\":12}"),
                            @ExampleObject(name = "Пример 2", summary = "Зерна кофе",
                                    value = "{\"name\":\"Зерно Бразилия, 250 г\",\"price\":750," +
                                            "\"vat\":20,\"menuGroupId\":2,\"stockItemId\":13}")
                    }))
            @RequestBody MenuItemStockDto dto
    ) {
        return ResponseEntity.ok(menuService.createMenuItemWithStockItem(dto));
    }

    @Operation(summary = "Создание позиции меню (с техкартой)",
            description = "Возвращает созданную позицию меню")
    @PostMapping("/item/pc")
    public ResponseEntity<MenuItem> createMenuItemWithProcessChart(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
                    "Данные для создания позиции меню и связанной техкарты",
                    content = @Content(examples = {@ExampleObject(
                            value = "{\"name\":\"лимонад с лимоном и огурцом\",\"price\":180," +
                                    "\"vat\":10,\"menuGroupId\":4,\"processChart\":" +
                                    "{\"description\":\"Насыпать лед, положить дольку лимона и нарезанный " +
                                    "слайсами огурец, залить минералкой.\",\"yield\":300,\"portion\":1}," +
                                    "\"recipeCompositions\":[" +
                                    "{\"netto\":150,\"ingredientId\":2,\"semiFinishedId\":null}," +
                                    "{\"netto\":20,\"ingredientId\":3,\"semiFinishedId\":null}," +
                                    "{\"netto\":30,\"ingredientId\":10,\"semiFinishedId\":null}," +
                                    "{\"netto\":130,\"ingredientId\":13,\"semiFinishedId\":null}]}"
                    )}))
            @RequestBody MenuItemPCDto dto
    ) {
        return ResponseEntity.ok(menuService.createMenuItemWithProcessChart(dto));
    }

    @Operation(summary = "Удаление группы меню по ID",
            description = "Удаление группы меню и входящих позиций меню")
    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteMenuGroup(
            @Parameter(name = "id", description = "MenuGroup id", example = "1")
            @PathVariable("id") int id
    ) {
        menuService.deleteMenuGroupById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление позиции меню по ID",
            description = "Мягкое удаление позиции меню")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @Parameter(name = "id", description = "MenuItem id", example = "1")
            @PathVariable("id") long id
    ) {
        menuService.deleteMenuItemById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Получение сгруппированного списка позиций меню, для которых есть техкарта",
            description = "Список групп и неудаленных позиций меню, которые готовятся на предприятии, " +
                    "отсортирован по имени группы и по имени позиции меню внутри группы")
    @GetMapping("/cook")
    public ResponseEntity<List<CookGroupDto>> getProductionList() {
        return ResponseEntity.ok(menuService.getProductionList());
    }

}
