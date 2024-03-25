package com.kirin.outlet.service;

import com.kirin.outlet.model.ProcessChart;
import com.kirin.outlet.repository.MenuGroupRepo;
import com.kirin.outlet.repository.MenuItemRepo;
import com.kirin.outlet.repository.ProcessChartRepo;
import com.kirin.outlet.repository.RecipeCompositionRepo;
import com.kirin.outlet.repository.SemiFinishedRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

/**
 * Сервис для создания и управления позициями меню, включая работу с техкартами,
 * полуфабрикатами и рецептурными компонентами
 */
@Service
@RequiredArgsConstructor
public class CookingService {

    private final MenuGroupRepo menuGroupRepo;

    private final MenuItemRepo menuItemRepo;

    private final ProcessChartRepo processChartRepo;

    private final SemiFinishedRepo semiFinishedRepo;

    private final RecipeCompositionRepo recipeCompositionRepo;

    // public HashMap<Long, BigDecimal> calcNecessaryIngrForCook(
    //         @NonNull HashMap<Long, Integer> processCharts) {
    //     Optional<ProcessChart> processChart;
    //     for (Long pcId : processCharts.keySet()) {
    //         processChart = processChartRepo.findById(pcId);
    //
    //     }
    //
    //
    //     return null;
    // }
}
