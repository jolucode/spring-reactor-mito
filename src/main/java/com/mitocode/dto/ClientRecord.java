package com.mitocode.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientRecord(

        String id,

        @NotBlank(message = "nameClient must not be blank")
        @Size(min = 3, message = "nameClient must have at least 3 characters")
        String nameClient,

        @NotBlank(message = "surnameClient must not be blank")
        @Size(min = 3, message = "surnameClient must have at least 3 characters")
        String surnameClient,

        @NotNull(message = "birthDateClient must not be null")
        LocalDate birthDateClient,

        @NotBlank(message = "urlPhotoClient must not be blank")
        String urlPhotoClient

) {}
