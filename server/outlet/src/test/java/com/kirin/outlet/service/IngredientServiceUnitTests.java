package com.kirin.outlet.service;

import com.kirin.outlet.model.Ingredient;
import com.kirin.outlet.model.exception.ItemNotFoundException;
import com.kirin.outlet.repository.IngredientRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceUnitTests {

    /**
     * Экземпляр сервиса, который будет тестироваться
     */
    @InjectMocks
    private IngredientService ingredientService;

    /**
     * Разрываем связь и создаем mock для репозитория (заглушку)
     */
    @Mock
    private IngredientRepo mockRepo;

    @Test
    void getIngredientByIdFoundTest() {
        Ingredient ingredient = new Ingredient("Огурец", 2, 12, 1L);
        ingredient.setId(1L);
        given(mockRepo.findById(1L)).willReturn(Optional.of(ingredient));

        Ingredient ingredient1 = ingredientService.getIngredientById(1L);

        verify(mockRepo, only()).findById(1L);
        assertThat(ingredient1).isEqualTo(ingredient);
    }

    @Test
    void getIngredientByIdNotFoundTest() {
        given(mockRepo.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(
                ItemNotFoundException.class,
                () -> ingredientService.getIngredientById(anyLong())
        );
    }


}
