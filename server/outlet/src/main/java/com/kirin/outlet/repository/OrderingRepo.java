package com.kirin.outlet.repository;

import com.kirin.outlet.model.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderingRepo extends JpaRepository<Ordering, Long> {

    /**
     * Поиск заказа по уникальному ID чека.
     * @param receiptId ID чека
     * @return Optional объект заказа
     */
    Optional<Ordering> findByReceiptId(Long receiptId);

    /**
     * Получение списка заказов, созданных в определенную дату.
     * @param date дата для поиска
     * @return список найденных заказов
     */
    @Query(value = "select * from ordering o where cast(o.created_on as date) = :date",
            nativeQuery = true)
    List<Ordering> findOrdersByDate(LocalDate date);
}
