package com.kirin.outlet.repository;

import com.kirin.outlet.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {
}
