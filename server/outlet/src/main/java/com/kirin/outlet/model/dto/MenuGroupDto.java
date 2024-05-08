package com.kirin.outlet.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO группы меню, хранящий имя
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuGroupDto {

    /**
     * Название группы
     */
    @NotBlank
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё]{2,}([ -][A-Za-zА-ЯЁа-яё]+)*")
    private String name;

}
