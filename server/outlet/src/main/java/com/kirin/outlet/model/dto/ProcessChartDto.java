package com.kirin.outlet.model.dto;

import com.kirin.outlet.model.ProcessChart;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
     * Данные о рецептурном компоненте. Ключ - ID компонента, значение - информация об ингредиенте
     */
    private Map<Long, IngredientOfRecipeDto> components;

}
