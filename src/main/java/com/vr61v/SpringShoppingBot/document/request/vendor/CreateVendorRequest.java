package com.vr61v.SpringShoppingBot.document.request.vendor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVendorRequest(

        @NotBlank @NotNull
        String name,

        @NotBlank @NotNull
        Boolean description

) {
}
