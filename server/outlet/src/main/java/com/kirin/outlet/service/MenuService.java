package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.StockItem;
import com.kirin.outlet.model.dto.MenuGroupDto;
import com.kirin.outlet.model.dto.MenuItemStockDto;
import com.kirin.outlet.model.exception.IncorrectDataInDatabaseException;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.MenuGroupRepo;
import com.kirin.outlet.repository.MenuItemRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepo menuItemRepo;

    private final MenuGroupRepo menuGroupRepo;

    private final StockService stockService;

    /**
     * Получения списка групп меню, отсортированного по имени группы.
     *
     * @return отсортированный список групп меню
     */
    public List<MenuGroup> getMenuGroupsList() {
        List<MenuGroup> menuGroups = menuGroupRepo.findAllByDeletedAtIsNull();
        menuGroups.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return menuGroups;
    }

    /**
     * Получение отсортированного по имени позиции списка позиций меню в указанной группе
     * по ID группы. Группа может быть пустой.
     *
     * @param groupId ID группы меню
     * @return отсортированный список позиций меню в группе, может быть пустым
     */
    public List<MenuItem> getMenuItemsInGroup(@Positive Integer groupId) {
        if (getMenuGroupById(groupId).getDeletedAt() != null)
            throw new IncorrectRequestDataException("Группа меню с ID = " + groupId + " была удалена");

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
     * Получение позиции меню по ID.
     *
     * @param id уникальный идентификатор позиции меню
     * @return позицию меню с указанным ID
     */
    public MenuItem getMenuItemById(@Positive Long id) {
        Optional<MenuItem> menuItem = menuItemRepo.findById(id);
        if (menuItem.isEmpty())
            throw new ItemNotFoundException("Позиция меню с ID = " + id + " не найдена");
        return menuItem.get();
    }

    /**
     * Получение неудаленной группы меню по ID.
     *
     * @param id уникальный идентификатор группы меню
     * @return группа меню с указанным ID
     */
    public MenuGroup getMenuGroupById(@Positive Integer id) {
        Optional<MenuGroup> menuGroup = menuGroupRepo.findByIdAndDeletedAtIsNull(id);
        if (menuGroup.isEmpty())
            throw new ItemNotFoundException("Группа меню с ID = " + id + " не найдена");
        return menuGroup.get();
    }

    /**
     * Добавление новой группы меню. Если уже есть неудаленная группа с таким же именем
     * (игнорируя регистр), то возвращается она. Если есть удаленная группа с таким же именем,
     * то она помечается неудаленной, заменяется имя на то, что пришло в dto, и запись обновляется.
     * Если группа с указанным именем не найдена, будет создана новая.
     *
     * @param dto объект группы меню с именем новой группы
     * @return объект группы меню с присвоенным ID
     */
    public MenuGroup createMenuGroup(@Valid MenuGroupDto dto) {
        List<MenuGroup> findMenuGroups = menuGroupRepo.findByNameIgnoreCase(dto.getName());
        if (findMenuGroups.size() == 1 && !findMenuGroups.get(0).isDeleted())
            return findMenuGroups.get(0);
        String name = Character.toUpperCase(dto.getName().charAt(0))
                + dto.getName().substring(1);
        MenuGroup menuGroup;
        if (findMenuGroups.isEmpty()) menuGroup = new MenuGroup();
        else if (findMenuGroups.size() == 1 && findMenuGroups.get(0).isDeleted()) {
            menuGroup = findMenuGroups.get(0);
            menuGroup.cancelDelete();
        } else throw new IncorrectDataInDatabaseException("Значение name = '" + dto.getName()
                + "' в таблице menu_group среди неудаленных позиций не уникально");
        menuGroup.setName(name);
        return menuGroupRepo.save(menuGroup);
    }

    /**
     * Удаление группы меню по ID и входящих в группу позиций меню.
     *
     * @param id уникальный идентификатор группы меню
     */
    public void deleteMenuGroupById(@Positive int id) {
        MenuGroup menuGroup = getMenuGroupById(id);
        menuGroup.setDelete();
        menuGroupRepo.save(menuGroup);
        // TODO: удаление позиций меню
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
        long sameId = getIdSameDeletedMenuItem(menuItem);
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
            throw new IncorrectRequestDataException("Позиция на складе должна быть штучной");
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
        if (menuItems.size() == 1)
            throw new IncorrectRequestDataException("Уже существует позиция меню с именем '"
                    + name + "' (ID = " + menuItems.get(0).getId() + ")");
        else if (menuItems.size() > 1)
            throw new IncorrectDataInDatabaseException(
                    "Значение name = '" + name
                            + "' в таблице menu_item среди неудаленных позиций не уникально");
    }

    /**
     * Проверка на отсутствие в репозитории неудаленной позиции меню, связанной с указанной
     * позицией на складе по ID.
     *
     * @param stockItemId ID позиции на складе для проверки уникальности
     */
    private void checkUniqueStockItemId(@Positive long stockItemId) {
        List<MenuItem> items = menuItemRepo.findByStockItemIdAndDeletedAtIsNull(stockItemId);
        if (items.size() == 1) throw new IncorrectRequestDataException(
                "Уже существует позиция меню (ID = " + items.get(0).getId()
                        + ") для продажи позиции на складе с ID = " + stockItemId);
        else if (items.size() > 1) throw new IncorrectDataInDatabaseException(
                "Значение stock_item_id = " + stockItemId
                        + " в таблице menu_item среди неудаленных позиций не уникально");
    }

}
