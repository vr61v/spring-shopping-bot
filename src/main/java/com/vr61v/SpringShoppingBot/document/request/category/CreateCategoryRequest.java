package com.vr61v.SpringShoppingBot.document.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(

        @NotBlank @NotNull
        String name,

        @NotBlank @NotNull
        Boolean isForOverEighteen

) {
}
