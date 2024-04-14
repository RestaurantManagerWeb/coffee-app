package com.kirin.outlet.repository;

import com.kirin.outlet.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockItemRepo extends JpaRepository<StockItem, Long> {

    /**
     * Получение списка позиций меню по ID единицы измерения.
     * @param unitMeasureId ID единицы измерения
     * @return список найденных позиций
     */
    List<StockItem> findByUnitMeasureIdIs(Integer unitMeasureId);

}
