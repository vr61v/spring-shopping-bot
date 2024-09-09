package com.vr61v.SpringShoppingBot.entity.request.category;

public record UpdateCategoryRequest(

        String name,

        Boolean isForOverEighteen

) {
}
