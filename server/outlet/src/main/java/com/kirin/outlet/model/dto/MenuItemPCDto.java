package com.kirin.outlet.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для создания позиции меню и связанной с ней техкарты
 */
@Getter
public class MenuItemPCDto {

    /**
     * Конструктор для инициализации данных для создания позиции меню и связанной с ней техкарты.
     *
     * @param name               название позиции меню
     * @param price              цена в рублях
     * @param vat                НДС в %. По умолчанию 10%
     * @param menuGroupId        ID группы меню
     * @param processChart       данные для создания техкарты
     * @param recipeCompositions данные для создания списка рецептурных компонентов
     */
    public MenuItemPCDto(String name, BigDecimal price, Integer vat, int menuGroupId,
                         ProcessChartNewDto processChart,
                         List<RecipeCompositionNewDto> recipeCompositions
    ) {
        this.name = name;
        this.price = price;
        this.vat = vat;
        this.menuGroupId = menuGroupId;
        this.processChart = processChart;
        this.recipeCompositions = recipeCompositions;
    }

    /**
     * Название позиции меню
     */
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}(([ /-]|(, ))?[\\dA-Za-zА-ЯЁа-яё]+\\.?)*")
    private String name;

    /**
     * Цена в рублях
     */
    @DecimalMin(value = "1.0")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    /**
     * НДС в %. По умолчанию 10%
     */
    @Min(0)
    @Max(100)
    private Integer vat;

    /**
     * ID группы меню
     */
    @Positive
    private int menuGroupId;

    /**
     * Данные для создания техкарты
     */
    @Valid
    private ProcessChartNewDto processChart;

    /**
     * Данные для создания списка рецептурных компонентов
     */
    @NotEmpty
    @Valid
    private List<RecipeCompositionNewDto> recipeCompositions;

}
