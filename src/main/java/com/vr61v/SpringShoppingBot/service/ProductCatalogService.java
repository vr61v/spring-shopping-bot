package com.vr61v.SpringShoppingBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProductCatalogService {

    void openProductCatalog(Update update);

    void prevPage(Update update);

    void nextPage(Update update);

    void addProductInBucket(Update update);

    void addProductInFavourites(Update update);

    void closeProductCatalog(Update update);

}
