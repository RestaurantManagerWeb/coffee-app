package com.kirin.outlet.model.dto;

import com.kirin.outlet.model.ProcessChart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Данные о техкарте и списке ингредиентов
 */
@Getter
@Setter
public class ProcessChartDto {

    /**
     * Технологическая карта, содержащая данные о способе приготовления,
     * выходе (в граммах) и количестве порций
     */
    private ProcessChart processChart;

    /**
     * Данные о рецептурных компонентах в порядке их отображения
     */
    private List<IngredientOfRecipeDto> components;

}
