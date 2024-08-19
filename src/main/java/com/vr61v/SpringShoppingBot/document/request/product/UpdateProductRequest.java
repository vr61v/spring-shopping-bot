package com.vr61v.SpringShoppingBot.document.request.product;

import jakarta.validation.constraints.Min;

import java.util.UUID;

public record UpdateProductRequest(

        String name,

        @Min(1)
        Float price,

        @Min(1)
        Integer count,

        String description,

        UUID categoryId,

        UUID vendorId

) {
}
