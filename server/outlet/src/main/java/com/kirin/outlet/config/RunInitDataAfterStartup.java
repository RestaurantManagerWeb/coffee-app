package com.kirin.outlet.config;

import com.kirin.outlet.model.UnitMeasure;
import com.kirin.outlet.repository.UnitMeasureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Добавление в базу данных константных значений
 */
@Component
@RequiredArgsConstructor
public class RunInitDataAfterStartup {

    private final UnitMeasureRepo unitMeasureRepo;

    /**
     * Добавление в пустую базу данных сведений об используемых в приложении единицах измерения
     */
    @Order(1)
    @EventListener(ApplicationReadyEvent.class)
    public void testDataUnitMeasure() {
        if (unitMeasureRepo.count() == 0) {
            unitMeasureRepo.save(new UnitMeasure("масса", "г"));
            unitMeasureRepo.save(new UnitMeasure("объем", "мл"));
            unitMeasureRepo.save(new UnitMeasure("количество", "шт."));
        }
    }
}
