package com.kirin.outlet.service;

import com.kirin.outlet.model.MenuGroup;
import com.kirin.outlet.model.MenuItem;
import com.kirin.outlet.model.exception.IncorrectRequestDataException;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.MenuGroupRepo;
import com.kirin.outlet.repository.MenuItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepo menuItemRepo;

    private final MenuGroupRepo menuGroupRepo;

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
     * Получение отсортированного по имени позиции списка позиций меню в указанной группе
     * по ID группы. Группа может быть пустой.
     *
     * @param groupId ID группы меню
     * @return отсортированный список позиций меню в группе, может быть пустым
     */
    public List<MenuItem> getMenuItemsInGroup(Integer groupId) {
        getMenuGroupById(groupId);
        List<MenuItem> menuItems = menuItemRepo.findByMenuGroupId(groupId);
        menuItems.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return menuItems;
    }

    /**
     * Получение списка всех позиций меню.
     *
     * @return список позиций меню
     */
    public List<MenuItem> getMenuItems() {
        return menuItemRepo.findAll();
    }

    /**
     * Получение позиции меню по ID.
     *
     * @param id уникальный идентификатор позиции меню
     * @return позицию меню с указанным ID
     */
    public MenuItem getMenuItemById(Long id) {
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
    public MenuGroup getMenuGroupById(Integer id) {
        Optional<MenuGroup> menuGroup = menuGroupRepo.findById(id);
        if (menuGroup.isEmpty())
            throw new ItemNotFoundException("Группа меню с ID = " + id + " не найдена");
        return menuGroup.get();
    }

    /**
     * Добавление новой группы меню.
     * @param menuGroup объект группы меню с именем новой группы
     * @return объект группы меню с присвоенным ID
     */
    public MenuGroup createMenuGroup(MenuGroup menuGroup) {
        String name = checkAndGetCorrectMenuGroupName(menuGroup.getName());
        MenuGroup newGroup = new MenuGroup();
        newGroup.setName(name);
        try {
            return menuGroupRepo.save(newGroup);
        } catch (RuntimeException e) {
            throw new IncorrectRequestDataException(
                    "Нельзя создать группу меню с именем '" + menuGroup.getName() + "'");
        }
    }

    /**
     * Проверка имени группы меню на корректность и получение имени с первой прописной буквой.
     * Имя должно иметь длину от 3 до 30 символов включительно, состоять из символов русского
     * и латинского алфавита, допустимо разделение слов через пробел или дефис. Имя должно быть
     * уникальным, не зарегистрированным в репозитории.
     * @param name имя группы для проверки
     * @return валидное имя группы, начинающееся с прописной буквы
     */
    private String checkAndGetCorrectMenuGroupName(String name) {
        if (name == null || name.isEmpty())
            throw new IncorrectRequestDataException("Имя для новой группы не задано");
        name = name.trim();
        if (name.length() < 3)
            throw new IncorrectRequestDataException(
                    "Длина имени группы меню не должна быть менее 3 символов");
        if (name.length() > 30)
            throw new IncorrectRequestDataException(
                    "Длина имени группы меню не должна превышать 30 символов");
        if (!name.matches("[A-Za-zА-ЯЁа-яё]{2,}([\s\\-][A-Za-zА-ЯЁа-яё]+)*"))
            throw new IncorrectRequestDataException("Имя группы меню должно состоять только из символов " +
                    "латинского и русского алфавитов, слова можно разделять пробелом или дефисом");
        if (menuGroupRepo.findByNameIgnoreCase(name).isPresent())
            throw new IncorrectRequestDataException("Группа меню с именем '" + name + "' уже существует");

        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
