package com.kirin.outlet.repository;

import com.kirin.outlet.model.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderingRepo extends JpaRepository<Ordering, Long> {

    /**
     * Поиск заказа по уникальному ID чека.
     * @param receiptId ID чека
     * @return Optional объект заказа
     */
    Optional<Ordering> findByReceiptId(Long receiptId);
}
