package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * Данные для создания ингредиента
 */
@Getter
public class IngredientDto {

    /**
     * Конструктор для инициализации данных для создания ингредиента.
     *
     * @param name               уникальное название ингредиента
     * @param processingMethodId ID связанного метода обработки
     * @param weightLoss         процент потерь при обработке
     * @param stockItemId        ID связанной позиции на складе. Может быть null
     */
    public IngredientDto(String name, int processingMethodId, int weightLoss, Long stockItemId) {
        this.name = name;
        this.processingMethodId = processingMethodId;
        this.weightLoss = weightLoss;
        this.stockItemId = stockItemId;
    }

    /**
     * Уникальное название ингредиента
     */
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}(([ /-]|(, ))?[\\dA-Za-zА-ЯЁа-яё]+\\.?)*")
    private String name;

    /**
     * ID связанного метода обработки
     */
    @Positive
    private int processingMethodId;

    /**
     * Процент потерь при обработке
     */
    @Min(0)
    @Max(100)
    private int weightLoss;

    /**
     * ID связанной позиции на складе. Может быть null
     */
    @Min(1)
    private Long stockItemId;

}
