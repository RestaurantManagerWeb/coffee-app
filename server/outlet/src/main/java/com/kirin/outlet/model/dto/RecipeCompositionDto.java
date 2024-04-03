package com.kirin.outlet.model.dto;

import com.kirin.outlet.model.RecipeComposition;
import lombok.Getter;

/**
 * Информация о рецептурном компоненте
 */
@Getter
public class RecipeCompositionDto implements Comparable<RecipeCompositionDto> {

    /**
     * Конструктор для задания информации о входящем в состав ингредиенте.
     * @param recipeComposition рецептурный компонент
     * @param name название ингредиента
     * @param isPieceUnit является ли ингредиент штучным
     */
    public RecipeCompositionDto(RecipeComposition recipeComposition,
                                String name, boolean isPieceUnit) {
        this.recipeComposition = recipeComposition;
        this.name = name;
        this.isPieceUnit = isPieceUnit;
    }

    /**
     * Конструктор для задания информации о входящем в состав полуфабрикате.
     * @param recipeComposition рецептурный компонент
     * @param name название полуфабриката
     */
    public RecipeCompositionDto(RecipeComposition recipeComposition, String name) {
        this.recipeComposition = recipeComposition;
        this.name = name;
    }

    /**
     * Рецептурный компонент
     */
    private RecipeComposition recipeComposition;

    /**
     * Название рецептурного компонента
     */
    private String name;

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
    public int compareTo(RecipeCompositionDto o) {
        return Long.compare(this.recipeComposition.getId(), o.recipeComposition.getId());
    }

}
