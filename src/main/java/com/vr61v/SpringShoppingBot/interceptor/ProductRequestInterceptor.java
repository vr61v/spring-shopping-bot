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

    private List<String> getCurrentPageInAlphabetOrder(Integer number) {
        ResponseEntity<List<Product>> productsResponse = productController.getAllProducts();
        if (productsResponse.getBody() == null) return List.of();
        List<Product> products = productsResponse.getBody();
        products.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));

        totalCountOfPages = (int) Math.ceil((double) products.size() / COUNT_OF_PRODUCT_ON_PAGE);
        int start = (number - 1) * COUNT_OF_PRODUCT_ON_PAGE, end = start + COUNT_OF_PRODUCT_ON_PAGE;
        if (end > products.size()) end = products.size();
        List<String> page = new ArrayList<>();
        for (Product product : products.subList(start, end)) {
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
        List<String> products = getCurrentPageInAlphabetOrder(START_CURRENT_PAGE);

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
        List<String> productNames = getCurrentPageInAlphabetOrder(newCurrentPage);

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
        List<String> productNames = getCurrentPageInAlphabetOrder(newCurrentPage);

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
