package com.kirin.outlet.service;

import com.kirin.outlet.model.Ingredient;
import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.dto.CookGroupDto;
import com.kirin.outlet.model.dto.CookMenuItemDto;
import com.kirin.outlet.model.dto.MenuGroupDto;
import com.kirin.outlet.model.dto.MenuItemPCDto;
import com.kirin.outlet.model.dto.MenuItemStockDto;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.MenuGroupRepo;
import com.kirin.outlet.repository.MenuItemRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepo menuItemRepo;

    private final MenuGroupRepo menuGroupRepo;

    private final StockService stockService;

    private final CookingService cookingService;

    /**
     * Получения списка групп меню, отсортированного по имени группы.
     *
     * @return отсортированный список групп меню
     */
    public List<MenuGroup> getMenuGroupsList() {
        List<MenuGroup> menuGroups = menuGroupRepo.findAll();
        menuGroups.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return menuGroups;
    }

    /**
     * Получение отсортированного по имени позиции списка неудаленных позиций меню
     * в указанной группе по ID группы. Группа может быть пустой.
     *
     * @param groupId ID группы меню
     * @return отсортированный список позиций меню в группе, может быть пустым
     */
    public List<MenuItem> getMenuItemsInGroup(@Positive int groupId) {
        getMenuGroupById(groupId);
        List<MenuItem> menuItems = menuItemRepo.findByMenuGroupIdAndDeletedAtIsNull(groupId);
        menuItems.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return menuItems;
    }

    /**
     * Получение списка всех неудаленных позиций меню.
     *
     * @return список позиций меню
     */
    public List<MenuItem> getMenuItems() {
        return menuItemRepo.findAllByDeletedAtIsNull();
    }

    /**
     * Получение неудаленной позиции меню по ID.
     *
     * @param id уникальный идентификатор позиции меню
     * @return позицию меню с указанным ID
     */
    public MenuItem getMenuItemById(@Positive long id) {
        Optional<MenuItem> menuItem = menuItemRepo.findByIdAndDeletedAtIsNull(id);
        if (menuItem.isEmpty())
            throw new ItemNotFoundException("Позиция меню с ID = " + id + " не найдена либо удалена");
        return menuItem.get();
    }

    /**
     * Получение позиции меню по ID.
     *
     * @param id уникальный идентификатор позиции меню
     * @return позицию меню с указанным ID
     */
    public MenuItem getMenuItemWithDeletedById(@Positive long id) {
        Optional<MenuItem> menuItem = menuItemRepo.findById(id);
        if (menuItem.isEmpty())
            throw new ItemNotFoundException("Позиция меню с ID = " + id + " не найдена");
        return menuItem.get();
    }

    /**
     * Получение группы меню по ID.
     *
     * @param id уникальный идентификатор группы меню
     * @return группа меню с указанным ID
     */
    public MenuGroup getMenuGroupById(@Positive int id) {
        Optional<MenuGroup> menuGroup = menuGroupRepo.findById(id);
        if (menuGroup.isEmpty())
            throw new ItemNotFoundException("Группа меню с ID = " + id + " не найдена");
        return menuGroup.get();
    }

    /**
     * Добавление новой группы меню. Если уже есть группа с таким же именем (игнорируя регистр),
     * то возвращается она. Иначе будет создана новая.
     *
     * @param dto объект группы меню с именем новой группы
     * @return объект группы меню с присвоенным ID
     */
    public MenuGroup createMenuGroup(@Valid MenuGroupDto dto) {
        List<MenuGroup> findMenuGroups = menuGroupRepo.findByNameIgnoreCase(dto.getName());
        if (findMenuGroups.size() > 0)
            return findMenuGroups.get(0);
        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupRepo.save(menuGroup);
    }

    /**
     * Удаление группы меню по ID и входящих в группу позиций меню.
     *
     * @param id уникальный идентификатор группы меню
     */
    @Transactional
    public void deleteMenuGroupById(@Positive int id) {
        MenuGroup menuGroup = getMenuGroupById(id);
        deleteMenuItemsInGroup(id);
        menuGroupRepo.delete(menuGroup);
    }

    /**
     * Мягкое удаление позиций меню, входящих в группу по ID группы меню.
     *
     * @param groupId уникальный идентификатор группы меню
     */
    private void deleteMenuItemsInGroup(int groupId) {
        List<MenuItem> items = menuItemRepo.findByMenuGroupIdAndDeletedAtIsNull(groupId);
        for (MenuItem item : items) {
            deleteMenuItem(item);
        }
    }

    /**
     * Мягкое удаление позиции меню.
     *
     * @param item позиция меню для удаления
     */
    private void deleteMenuItem(MenuItem item) {
        item.setDelete();
        item.setMenuGroupId(null);
        menuItemRepo.save(item);
    }

    /**
     * Мягкое удаление позиции меню по ID.
     *
     * @param id уникальный идентификатор позиции меню, которую нужно удалить
     */
    public void deleteMenuItemById(@Positive long id) {
        MenuItem item = getMenuItemById(id);
        deleteMenuItem(item);
    }

    /**
     * Создание позиции меню, связанной со штучной позицией на складе. Входные данные сначала
     * проходят проверку на актуальность. Если данные актуальны и в репозитории есть удаленная
     * позиция с такими же именем (игнорируя регистр), ценой, НДС и ID позиции на складе, то
     * позиция меню с данным ID обновляется. Если позиции меню с указанным именем (игнорируя
     * регистр) не найдены, будет создана новая.
     *
     * @param dto данные для создания позиции меню, связанной со штучной позицией на складе
     * @return созданная или обновленная позиция меню
     */
    public MenuItem createMenuItemWithStockItem(@Valid MenuItemStockDto dto) {
        checkCorrectMenuItemStockDto(dto);
        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        MenuItem menuItem = new MenuItem(
                name, dto.getPrice(), dto.getVat(), dto.getMenuGroupId(), dto.getStockItemId());
        long sameId = getIdSameDeletedMenuItem(menuItem); // TODO: сократить количество запросов
        if (sameId > 0) menuItem.setId(sameId);
        return menuItemRepo.save(menuItem);
    }

    /**
     * Поиск среди удаленных позиций меню записи со значениями имени (игнорируя регистр),
     * цены, НДС, ID позиции на складе и ID техкарты совпадающими с соответствующими полями
     * указанной позиции меню.
     *
     * @param menuItem позиция меню с данными для сравнения
     * @return ID найденной позиции меню или -1, если позиция не найдена
     */
    private long getIdSameDeletedMenuItem(MenuItem menuItem) {
        List<MenuItem> findByName = menuItemRepo.findByNameIgnoreCase(menuItem.getName());
        if (!findByName.isEmpty()) {
            Optional<MenuItem> delSameItem = findByName.stream()
                    .filter(item -> item.getPrice().equals(menuItem.getPrice())
                            && item.getVat().equals(menuItem.getVat())
                            && item.getStockItemId().equals(menuItem.getStockItemId())
                            && item.getProcessChartId().equals(menuItem.getProcessChartId())
                            && item.isDeleted()
                    ).findFirst();
            if (delSameItem.isPresent())
                return delSameItem.get().getId();
        }
        return -1;
    }

    /**
     * Проверка на валидность данных для создания позиции меню, связанной с позицией на складе.
     * Должны быть указаны существующие группа меню, уникальное имя позиции, уникальная штучная
     * позиция на складе.
     *
     * @param dto данные для проверки
     */
    private void checkCorrectMenuItemStockDto(MenuItemStockDto dto) {
        getMenuGroupById(dto.getMenuGroupId());
        checkUniqueMenuItemName(dto.getName());
        StockItem stockItem = stockService.getStockItemById(dto.getStockItemId());
        // TODO: магические ед. измер.
        if (stockItem.getUnitMeasure().getId() != 3)
            throw new IncorrectRequestDataException("createMenuItemWithStockItem.dto.stockItemId",
                    "Позиция на складе должна быть штучной");
        checkUniqueStockItemId(dto.getStockItemId());
    }

    /**
     * Проверка на отсутствие в репозитории неудаленной позиции меню с указанным именем
     * (игнорируя регистр).
     *
     * @param name имя позиции меню для проверки на уникальность
     */
    private void checkUniqueMenuItemName(String name) {
        List<MenuItem> menuItems = menuItemRepo.findByNameIgnoreCaseAndDeletedAtIsNull(name);
        if (menuItems.size() > 0)
            throw new IncorrectRequestDataException(
                    "checkUniqueMenuItemName.name", "Имя позиции меню не уникально");
    }

    /**
     * Проверка на отсутствие в репозитории неудаленной позиции меню, связанной с указанной
     * позицией на складе по ID.
     *
     * @param stockItemId ID позиции на складе для проверки уникальности
     */
    private void checkUniqueStockItemId(long stockItemId) {
        List<MenuItem> items = menuItemRepo.findByStockItemIdAndDeletedAtIsNull(stockItemId);
        if (items.size() > 0) throw new IncorrectRequestDataException(
                "checkUniqueStockItemId.stockItemId",
                "Уже существует позиция меню (ID = " + items.get(0).getId()
                        + ") для продажи позиции на складе с ID = " + stockItemId);
    }

    /**
     * Получение списка позиций меню, для которых есть техкарта.
     * Список разбит по группам меню, для каждой позиции указаны имя и ID техкарты.
     * Список отсортирован по имени группы и по имени позиции меню внутри группы.
     *
     * @return список с информацией о позициях, которые готовятся на предприятии
     */
    public List<CookGroupDto> getProductionList() {
        List<MenuItem> menuItems = getMenuItems();
        Map<Integer, CookGroupDto> positions = new HashMap<>();
        Integer key;
        // TODO: добавлять MenuItem вместо CookMenuItemDto
        CookMenuItemDto cookItem;
        for (MenuItem item : menuItems) {
            if (item.getProcessChart() != null) {
                key = item.getMenuGroup().getId();
                cookItem = new CookMenuItemDto(
                        item.getId(), item.getName(), item.getProcessChart().getId());
                if (positions.containsKey(key))
                    positions.get(key).addItem(cookItem);
                else positions.put(key, new CookGroupDto(cookItem));
            }
        }
        setNamesCookGroups(positions);
        List<CookGroupDto> cookGroups = new ArrayList<>(positions.values());
        cookGroups.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return cookGroups;
    }

    /**
     * Устанавливает название группы по ID группы.
     *
     * @param positions HashMap с ID группы меню в качестве ключей
     */
    private void setNamesCookGroups(Map<Integer, CookGroupDto> positions) {
        List<MenuGroup> menuGroups = getMenuGroupsList();

        HashMap<Integer, String> groupsName = new HashMap<>();
        for (MenuGroup group : menuGroups) {
            groupsName.put(group.getId(), group.getName());
        }

        for (var cookGroup : positions.entrySet()) {
            cookGroup.getValue().setName(groupsName.get(cookGroup.getKey()));
        }
    }

    /**
     * Проверка данных и создание позиции меню, связанной с ней техкарты и рецептурных компонентов.
     *
     * @param dto данные для создания позиции меню, техкарты и рецептурных компонентов
     * @return созданная позиция меню
     */
    public MenuItem createMenuItemWithProcessChart(@Valid MenuItemPCDto dto) {
        checkCorrectMenuItemPCDto(dto);
        try {
            return createMenuItemAndProcessChart(dto);
        } catch (Exception e) {
            throw new CannotCreateTransactionException(
                    "Не удалось создать позицию меню. " + e.getMessage(), e);
        }
    }

    /**
     * Проверка на валидность данных для создания позиции меню и связанной техкарты.
     * Должны быть указаны существующие группа меню, уникальное имя позиции, валидные
     * данные для создания техкарты.
     *
     * @param dto данные для проверки
     */
    private void checkCorrectMenuItemPCDto(MenuItemPCDto dto) {
        getMenuGroupById(dto.getMenuGroupId());
        checkUniqueMenuItemName(dto.getName());
        cookingService.checkCorrectRecipeCompositions(dto.getRecipeCompositions());
    }

    /**
     * Транзакция. Создание связанных техкарты, рецептурных компонентов и позиции меню.
     *
     * @param dto данные для создания объектов
     * @return созданная позиция меню
     */
    @Transactional
    private MenuItem createMenuItemAndProcessChart(MenuItemPCDto dto) {
        long pcId = cookingService.createProcessChartAndRecipeCompositions(
                dto.getProcessChart(), dto.getRecipeCompositions());
        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        // TODO: проверка на равные удаленные позиции
        return menuItemRepo.save(new MenuItem(name, dto.getPrice(), dto.getVat(),
                pcId, dto.getMenuGroupId()));
    }

    /**
     * Проверка наличия всех указанных позиций меню по их ID.
     *
     * @param idSet множество ID позиций меню для проверки
     */
    public void checkPresentAllMenuItemsById(Set<Long> idSet) {
        List<MenuItem> menuItems = menuItemRepo.findByDeletedAtIsNullAndIdIn(idSet);
        if (menuItems.size() < idSet.size()) {
            idSet.removeAll(menuItems.stream()
                    .mapToLong(MenuItem::getId)
                    .boxed().collect(Collectors.toSet()));
            throw new ItemNotFoundException("Не найдены или удалены позиции меню с ID = " + idSet);
        }
    }

}
