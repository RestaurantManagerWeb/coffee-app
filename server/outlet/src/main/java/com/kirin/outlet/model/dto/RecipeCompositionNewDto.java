package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Данные для создания рецептурного компонента
 */
@Getter
public class RecipeCompositionNewDto {

    /**
     * Конструктор для инициализации данных для создания рецептурного компонента.
     *
     * @param netto          Норма расхода продукта в граммах или штуках
     * @param ingredientId   ID ингредиента
     * @param semiFinishedId ID полуфабриката
     */
    public RecipeCompositionNewDto(BigDecimal netto, Long ingredientId, Long semiFinishedId) {
        this.netto = netto;
        this.ingredientId = ingredientId;
        this.semiFinishedId = semiFinishedId;
    }

    /**
     * Норма расхода продукта в граммах или штуках (для штучных позиций на складе)
     */
    @DecimalMin(value = "0.1")
    @Digits(integer = 5, fraction = 1)
    private BigDecimal netto;

    /**
     * ID ингредиента
     */
    @Positive
    private Long ingredientId;

    /**
     * ID полуфабриката
     */
    @Positive
    private Long semiFinishedId;

    /**
     * Проверка, что указан только один из параметров: ingredientId или semiFinishedId.
     *
     * @return результат проверки
     */
    @AssertTrue(message = "Укажите только один из параметров: ingredientId или semiFinishedId")
    private boolean isValid() {
        return (ingredientId != null && semiFinishedId == null)
                || (ingredientId == null && semiFinishedId != null);
    }
}
