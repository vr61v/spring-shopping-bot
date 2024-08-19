package com.vr61v.SpringShoppingBot.document.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProductRequest(

        @NotBlank
        String name,

        @NotNull @Min(1)
        Float price,

        @NotNull @Min(1)
        Integer count,

        @NotNull @NotBlank
        String description,

        @NotNull
        UUID categoryId,

        @NotNull
        UUID vendorId

) {
}
