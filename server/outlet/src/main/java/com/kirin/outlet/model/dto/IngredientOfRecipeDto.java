package com.kirin.outlet.model.dto;

import lombok.Data;

/**
 * Информация рецептурном компоненте
 */
@Data
public class IngredientOfRecipeDto {

    /**
     * Конструктор для задания информации о входящем в состав полуфабрикате.
     * @param name название полуфабриката
     * @param netto масса нетто в граммах
     * @param semiFinishedId ID полуфабриката
     */
    public IngredientOfRecipeDto(String name, double netto, Long semiFinishedId) {
        this.name = name;
        this.netto = netto;
        this.semiFinishedId = semiFinishedId;
    }

    /**
     * Конструктор для задания информации о входящем в состав ингредиенте.
     * @param name название ингредиента
     * @param netto масса нетто в граммах или штуках
     * @param isPieceUnit является ли ингредиент штучным
     */
    public IngredientOfRecipeDto(String name, double netto, boolean isPieceUnit) {
        this.name = name;
        this.netto = netto;
        this.isPieceUnit = isPieceUnit;
    }

    /**
     * Название рецептурного компонента
     */
    private String name;

    /**
     * Количество в граммах или штуках
     */
    private double netto;

    /**
     * ID полуфабриката (null для ингредиента)
     */
    private Long semiFinishedId;

    /**
     * Является ли ингредиент штучным (всегда false для полуфабриката)
     */
    private boolean isPieceUnit;
}
