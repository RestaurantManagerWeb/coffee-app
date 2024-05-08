package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * DTO позиции меню, связанной со штучной позицией на складе
 */
@Getter
public class MenuItemStockDto {

    /**
     * Конструктор для инициализации данных для создания позиции меню, связанной со штучной
     * позицией на складе.
     *
     * @param name        название позиции меню
     * @param price       цена в рублях
     * @param vat         НДС в %. По умолчанию 10%
     * @param menuGroupId ID группы меню
     * @param stockItemId ID позиции на складе
     */
    public MenuItemStockDto(String name, BigDecimal price, Integer vat, int menuGroupId, long stockItemId) {
        this.name = name;
        this.price = price;
        this.vat = vat;
        this.menuGroupId = menuGroupId;
        this.stockItemId = stockItemId;
    }

    /**
     * Название позиции
     */
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}(([ /-]|(, ))?[\\dA-Za-zА-ЯЁа-яё]+\\.?)*")
    private String name;

    /**
     * Цена в рублях
     */
    @DecimalMin(value = "1.0")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    /**
     * НДС в %. По умолчанию 10%
     */
    @Min(0)
    @Max(100)
    private Integer vat;

    /**
     * ID группы меню
     */
    @Positive
    private int menuGroupId;

    /**
     * ID позиции на складе
     */
    @Positive
    private long stockItemId;

}
