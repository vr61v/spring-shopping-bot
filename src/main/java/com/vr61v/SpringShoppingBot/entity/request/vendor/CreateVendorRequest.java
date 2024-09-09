package com.vr61v.SpringShoppingBot.entity.request.vendor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVendorRequest(

        @NotBlank @NotNull
        String name,

        @NotBlank @NotNull
        String description

) {
}
