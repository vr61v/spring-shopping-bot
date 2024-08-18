package com.vr61v.SpringShoppingBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProductMenuService {

    void openProductMenu(Update update);

    void selectCategory(Update update);

    void searchProducts(String keyword);

    void closeProductMenu(Update update);

}
