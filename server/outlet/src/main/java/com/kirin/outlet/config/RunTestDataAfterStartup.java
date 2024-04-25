package com.kirin.outlet.config;

import com.kirin.outlet.model.Ordering;
import com.kirin.outlet.model.ShoppingCart;
import com.kirin.outlet.model.dto.*;
import com.kirin.outlet.repository.OrderingRepo;
import com.kirin.outlet.repository.ShoppingCartRepo;
import com.kirin.outlet.service.CookingService;
import com.kirin.outlet.service.IngredientService;
import com.kirin.outlet.service.MenuService;
import com.kirin.outlet.service.OrderService;
import com.kirin.outlet.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Тестовые данные для добавления в базу данных (профиль DEV)
 */
@Component
@RequiredArgsConstructor
public class RunTestDataAfterStartup {

    private final MenuService menuService;

    private final OrderService orderService;

    private final OrderingRepo orderingRepo;

    private final ShoppingCartRepo shoppingCartRepo;

    private final CookingService cookingService;

    private final IngredientService ingredientService;

    private final StockService stockService;

    /**
     * Добавление тестовых групп меню
     */
    @Profile("dev")
    @Order(2)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataMenuGroup() {
        menuService.createMenuGroup(new MenuGroupDto("Чай"));
        menuService.createMenuGroup(new MenuGroupDto("Покупные товары"));
        menuService.createMenuGroup(new MenuGroupDto("Сэндвичи"));
        menuService.createMenuGroup(new MenuGroupDto("лимонады"));
    }

    /**
     * Добавление тестовых позиций на складе
     */
    @Profile("dev")
    @Order(3)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataStockItem() {
        stockService.createStockItem(new StockItemDto("Лимон", 1));
        stockService.createStockItem(new StockItemDto("Чай черный листовой", 1));
        stockService.createStockItem(new StockItemDto("Черника с/м", 1));
        stockService.createStockItem(new StockItemDto("Черная смородина с/м", 1));
        stockService.createStockItem(new StockItemDto("Сахар белый", 1));
        stockService.createStockItem(new StockItemDto("Вода газ. 0.5 л пластик", 3));
        stockService.createStockItem(new StockItemDto("Хлеб тостовый", 3));
        stockService.createStockItem(new StockItemDto("Салатная смесь", 1));
        stockService.createStockItem(new StockItemDto("Огурец свежий", 1));
        stockService.createStockItem(new StockItemDto("Яйцо куриное", 3));
        stockService.createStockItem(new StockItemDto("Тунец консервированный рубленый", 1));
        stockService.createStockItem(new StockItemDto("Вода н/г 0.5 л пластик", 3));
        stockService.createStockItem(new StockItemDto("Зерно Бразилия 250 г", 3));
        stockService.createStockItem(new StockItemDto("Зерно Бразилия", 1));
        stockService.createStockItem(new StockItemDto("вода минеральная газ.", 2));
        List<StockItemQuantityDto> shipment = new LinkedList<>();
        shipment.add(new StockItemQuantityDto(1, 943));
        shipment.add(new StockItemQuantityDto(2, 280));
        shipment.add(new StockItemQuantityDto(3, 300));
        shipment.add(new StockItemQuantityDto(4, 420));
        shipment.add(new StockItemQuantityDto(5, 3400));
        shipment.add(new StockItemQuantityDto(6, 3));
        shipment.add(new StockItemQuantityDto(7, 24));
        shipment.add(new StockItemQuantityDto(8, 315));
        shipment.add(new StockItemQuantityDto(9, 227));
        shipment.add(new StockItemQuantityDto(10, 8));
        shipment.add(new StockItemQuantityDto(11, 483));
        shipment.add(new StockItemQuantityDto(12, 21));
        shipment.add(new StockItemQuantityDto(13, 3));
        shipment.add(new StockItemQuantityDto(14, 8700));
        shipment.add(new StockItemQuantityDto(15, 2400));
        stockService.acceptIncomingInventoryShipments(shipment);
    }

    /**
     * Добавление тестовых методов обработки
     */
    @Profile("dev")
    @Order(4)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataProcessingMethod() {
        ingredientService.createProcessingMethod(new ProcessingMethodDto(
                "без обработки", null));
        ingredientService.createProcessingMethod(new ProcessingMethodDto(
                "механическая обработка", "сортировка, мытье, очистка, промывание, нарезка"));
        ingredientService.createProcessingMethod(new ProcessingMethodDto(
                "тепловая (варка)", null));
        ingredientService.createProcessingMethod(new ProcessingMethodDto(
                "измельчение", null));
    }

    /**
     * Добавление тестовых ингредиентов
     */
    @Profile("dev")
    @Order(5)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataIngredient() {
        ingredientService.createIngredient(new IngredientDto("Вода питьевая", 1, 0, null));
        ingredientService.createIngredient(new IngredientDto("Лед", 1, 0, null));
        ingredientService.createIngredient(new IngredientDto("Лимон", 2, 12, 1L));
        ingredientService.createIngredient(new IngredientDto("Чай черный листовой", 1, 0, 2L));
        ingredientService.createIngredient(new IngredientDto("Черника", 1, 0, 3L));
        ingredientService.createIngredient(new IngredientDto("Черная смородина", 1, 0, 4L));
        ingredientService.createIngredient(new IngredientDto("Сахар", 1, 0, 5L));
        ingredientService.createIngredient(new IngredientDto("Хлеб тостовый", 1, 0, 7L));
        ingredientService.createIngredient(new IngredientDto("Салатная смесь", 2, 10, 8L));
        ingredientService.createIngredient(new IngredientDto("Огурец свежий", 2, 5, 9L));
        ingredientService.createIngredient(new IngredientDto("Яйцо куриное отварное", 3, 0, 10L));
        ingredientService.createIngredient(new IngredientDto("Тунец рубленый", 1, 22, 11L));
        ingredientService.createIngredient(new IngredientDto("Вода газ.", 1, 0, 15L));
    }

    /**
     * Добавление тестовых полуфабрикатов (с техкартой и списком рецептурных компонентов)
     */
    @Profile("dev")
    @Order(6)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataSemiFinished() {
        cookingService.createSemiFinished(new SemiFinishedDto("Пюре ягодное",
                new ProcessChartNewDto(500, null, "Замороженные ягоды, сахар и кипяток взбить до однородной массы в блендере, процедить через сито"),
                List.of(new RecipeCompositionNewDto(BigDecimal.valueOf(110), 5L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(110), 6L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(220), 7L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(80), 1L, null)))
        );
    }

    /**
     * Добавление тестовых позиций меню, связанных с позицией на складе или техкартой
     * (с созданием техкарты и списка рецептурных компонентов)
     */
    @Profile("dev")
    @Order(7)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataMenuItem() {
        menuService.createMenuItemWithProcessChart(new MenuItemPCDto(
                "Чай ягодный 300 мл", BigDecimal.valueOf(220), null, 1,
                new ProcessChartNewDto(300, null, "Добавить в стакан гостя дольку лимона, заварку в одноразовом пакетике, ягодное пюре и кипяток. Хорошо перемешать и удалить заварку"),
                List.of(new RecipeCompositionNewDto(BigDecimal.valueOf(15), 3L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(5), 4L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(20), null, 1L),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(260), 1L, null))
        ));
        menuService.createMenuItemWithProcessChart(new MenuItemPCDto(
                "Чай черный 200 мл", BigDecimal.valueOf(100), null, 1,
                new ProcessChartNewDto(200, null, "Добавить в стакан гостя заварку в одноразовом пакетике и кипяток до нужного объема"),
                List.of(new RecipeCompositionNewDto(BigDecimal.valueOf(4), 4L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(200), 1L, null))
        ));
        menuService.createMenuItemWithStockItem(new MenuItemStockDto(
                "Вода газ. 0.5 л пластик", BigDecimal.valueOf(100), 10, 2, 6));
        menuService.createMenuItemWithProcessChart(new MenuItemPCDto(
                "Тост с тунцом", BigDecimal.valueOf(130), null, 3,
                new ProcessChartNewDto(210, 2, "Тостовый хлеб подсушить на гриле, на один кусок положить равномерно зелень, огурец (слайсы), тунец, снова зелень, вареное яйцо, нарезанное кольцами. Затем накрыть вторым куском хлеба и разрезать по диагонали"),
                List.of(new RecipeCompositionNewDto(BigDecimal.valueOf(1), 8L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(10), 9L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(20), 10L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(50), 12L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(10), 9L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(1), 11L, null),
                        new RecipeCompositionNewDto(BigDecimal.valueOf(1), 8L, null))
        ));
    }

    /**
     * Добавление тестовых заказов
     */
    @Profile("dev")
    @Order(8)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataOrdering() {
        Ordering order = new Ordering(1L);
        order.setCreatedAt(Timestamp.valueOf("2024-01-15 14:22:23"));
        orderingRepo.save(order);
        shoppingCartRepo.save(new ShoppingCart(1L, 1L, 1));
        stockService.writeOffProductsFromStock(List.of(new ShopCartItemDto(1L, 1)));

        order = new Ordering(2L);
        order.setCreatedAt(Timestamp.valueOf("2024-01-15 20:22:23"));
        orderingRepo.save(order);
        shoppingCartRepo.save(new ShoppingCart(2L, 1L, 1));
        stockService.writeOffProductsFromStock(List.of(new ShopCartItemDto(1L, 1)));

        order = new Ordering(3L);
        order.setCreatedAt(Timestamp.valueOf("2024-01-18 21:22:23"));
        orderingRepo.save(order);
        shoppingCartRepo.save(new ShoppingCart(3L, 1L, 1));
        stockService.writeOffProductsFromStock(List.of(new ShopCartItemDto(1L, 1)));

        orderService.createNewOrdering(new OrderDto(4,
                List.of(new ShopCartItemDto(1, 1))));
        orderService.createNewOrdering(new OrderDto(5,
                List.of(new ShopCartItemDto(2, 1),
                        new ShopCartItemDto(1, 2))));
        orderService.createNewOrdering(new OrderDto(6,
                List.of(new ShopCartItemDto(4, 1))));
    }

}
