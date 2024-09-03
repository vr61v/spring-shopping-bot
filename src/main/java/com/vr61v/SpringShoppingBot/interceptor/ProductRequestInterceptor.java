package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.impl.ProductControllerTelegramBot;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.ui.ProductMenuUI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductRequestInterceptor {

    private final ProductControllerTelegramBot productController;

    private static int totalCountOfPages = 0;

    private final static int START_PREV_PAGE = 0;

    private final static int START_CURRENT_PAGE = 1;

    private final static int START_NEXT_PAGE = 2;

    private final static int COUNT_OF_PRODUCT_ON_PAGE = 5;

    private List<String> getProductPage(Integer from) {
        ResponseEntity<Long> productsCount = productController.getProductsCount();
        if (productsCount.getBody() == null) return List.of();
        totalCountOfPages = (int) Math.ceil((double) productsCount.getBody()  / COUNT_OF_PRODUCT_ON_PAGE);


        ResponseEntity<List<Product>> productsResponse;
        if (from + COUNT_OF_PRODUCT_ON_PAGE <= productsCount.getBody()) {
            productsResponse = productController.getProductPage(from, COUNT_OF_PRODUCT_ON_PAGE);
        } else {
            productsResponse = productController.getProductPage(from, Math.toIntExact(productsCount.getBody() - from));
        }
        if (productsResponse.getBody() == null) return List.of();
        List<Product> products = productsResponse.getBody();

        List<String> page = new ArrayList<>();
        for (Product product : products) {
            page.add(String.format("%s %.2fр. \n{id:`%s`}", product.getName(), product.getPrice(), product.getId()));
        }

        return page;
    }

    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();

        if (data.contains("OPEN_MENU")) {
            return openProductMenu(chatId);
        } else if (data.contains("GET_PREV_PAGE_")) {
            return getPrevPage(chatId, data);
        } else if (data.contains("GET_NEXT_PAGE_")) {
            return getNextPage(chatId, data);
        }

        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage openProductMenu(String chatId) {
        List<String> products = getProductPage(0);

        return ProductMenuUI.getProductUI(
                chatId,
                products,
                START_PREV_PAGE,
                START_CURRENT_PAGE,
                START_NEXT_PAGE,
                totalCountOfPages
        );
    }

    public SendMessage getPrevPage(String chatId, String data) {
        int newCurrentPage = Integer.parseInt(data.split("GET_PREV_PAGE_")[1]);
        int newPrevPage = newCurrentPage - 1;
        int newNextPage = newCurrentPage + 1;
        List<String> productNames = getProductPage((newCurrentPage - 1) * COUNT_OF_PRODUCT_ON_PAGE);

        return ProductMenuUI.getProductUI(
                chatId,
                productNames,
                newPrevPage,
                newCurrentPage,
                newNextPage,
                totalCountOfPages
        );
    }

    public SendMessage getNextPage(String chatId, String data) {
        int newCurrentPage = Integer.parseInt(data.split("GET_NEXT_PAGE_")[1]);
        int newPrevPage = newCurrentPage - 1;
        int newNextPage = newCurrentPage + 1;
        List<String> productNames = getProductPage((newCurrentPage - 1) * COUNT_OF_PRODUCT_ON_PAGE);

        return ProductMenuUI.getProductUI(
                chatId,
                productNames,
                newPrevPage,
                newCurrentPage,
                newNextPage,
                totalCountOfPages
        );
    }

}
