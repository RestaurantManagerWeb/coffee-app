package com.kirin.outlet.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

/**
 * Данные для создания полуфабриката и связанной с ним техкарты
 */
@Getter
public class SemiFinishedDto {

    /**
     * Конструктор для инициализации данных для создания полуфабриката и связанной с ним техкарты.
     *
     * @param name               Название полуфабриката
     * @param processChart
     * @param recipeCompositions
     */
    public SemiFinishedDto(String name, ProcessChartNewDto processChart,
                           List<RecipeCompositionNewDto> recipeCompositions) {
        this.name = name;
        this.processChart = processChart;
        this.recipeCompositions = recipeCompositions;
    }

    /**
     * Название полуфабриката
     */
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}(([ /-]|(, ))?[\\dA-Za-zА-ЯЁа-яё]+\\.?)*")
    private String name;

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
