package com.kirin.outlet.model.dto;

import lombok.Getter;

/**\
 * Информация рецептурном компоненте
 */
@Getter
public class IngredientOfRecipeDto implements Comparable<IngredientOfRecipeDto> {

    /**
     * Конструктор для задания информации о входящем в состав полуфабрикате.
     * @param rcId ID рецептурного компонента
     * @param name название полуфабриката
     * @param netto масса нетто в граммах
     * @param semiFinishedId ID полуфабриката
     */
    public IngredientOfRecipeDto(long rcId, String name, double netto, Long semiFinishedId) {
        recipeComposId = rcId;
        this.name = name;
        this.netto = netto;
        this.semiFinishedId = semiFinishedId;
    }

    /**
     * Конструктор для задания информации о входящем в состав ингредиенте.
     * @param rcId ID рецептурного компонента
     * @param name название ингредиента
     * @param netto масса нетто в граммах или штуках
     * @param isPieceUnit является ли ингредиент штучным
     */
    public IngredientOfRecipeDto(long rcId, String name, double netto, boolean isPieceUnit) {
        recipeComposId = rcId;
        this.name = name;
        this.netto = netto;
        this.isPieceUnit = isPieceUnit;
    }

    /**
     * ID рецептурного компонента
     */
    private long recipeComposId;

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
    private long semiFinishedId;

    /**
     * Является ли ингредиент штучным (всегда false для полуфабриката)
     */
    private boolean isPieceUnit;

    /**
     * Сравнение рецептурных компонентов по их ID.
     * @param o объект для сравнения с текущим
     * @return результат сравнения
     */
    @Override
    public int compareTo(IngredientOfRecipeDto o) {
        return Long.compare(this.recipeComposId, o.recipeComposId);
    }
}
