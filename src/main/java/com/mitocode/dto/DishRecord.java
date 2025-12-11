package com.mitocode.dto;

import jakarta.validation.constraints.*;

public record DishRecord(
        String id,

        @NotNull
        @NotEmpty
        @NotBlank
        @Size(min = 3)
        String nameDish,

        @Min(value = 1)
        @Max(value = 999)
        Double priceDish,

        @NotNull
        Boolean statusDish) {
}
