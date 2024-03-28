package com.kirin.outlet.repository;

import com.kirin.outlet.model.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderingRepo extends JpaRepository<Ordering, Long> {
}
