package com.kirin.outlet.repository;

import com.kirin.outlet.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepo extends JpaRepository<StockItem, Long> {
}
