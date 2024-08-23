package com.vr61v.SpringShoppingBot.document.request.category;

public record UpdateCategoryRequest(

        String name,

        Boolean isForOverEighteen

) {
}
