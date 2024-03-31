package com.kirin.outlet.repository;

import com.kirin.outlet.model.RecipeComposition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCompositionRepo extends JpaRepository<RecipeComposition, Long> {
}
