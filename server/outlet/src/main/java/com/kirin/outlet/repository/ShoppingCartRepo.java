package com.kirin.outlet.repository;

import com.kirin.outlet.model.ShoppingCartPK;
import com.kirin.outlet.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, ShoppingCartPK> {
}
