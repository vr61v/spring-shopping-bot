package com.vr61v.SpringShoppingBot.entity.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(

        @NotBlank @NotNull
        String name,

        @NotBlank @NotNull
        Boolean isForOverEighteen

) {
}
