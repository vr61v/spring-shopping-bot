package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.entity.Product;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.service.ProductService;
import com.vr61v.SpringShoppingBot.ui.ProductMenuUI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductRequestInterceptor {

    private final ProductService productService;

    private final static int COUNT_OF_PRODUCT_ON_PAGE = 1;

    private final static String URL = "https://spring-shopping-bot-bucket.hb.ru-msk.vkcloud-storage.ru/%s.png";

    /***
     *
     * @param chatId
     * @param query query for search product by field, this String may be empty.
     * @param from the product number that the page will start.
     * @param size count of product on page.
     * @return SendMediaGroup which contains a list of photos of the products and their
     * description, may contain a list of one photo, it needs to check it before executing it.
     */
    private SendMediaGroup openProductMenu(String chatId, String query, int from, int size) {
        List<Product> products;
        if (query.isEmpty()) {
            products = productService.getProductPage(from, size);
        } else {
            String[] split = query.split(":");
            products = productService.searchProductsByField(split[0], split[1], from, size);
        }

        List<String> descriptions = products.stream()
                .map(product -> String.format("%s %.2fр.\n{id:`%s`}", product.getName(), product.getPrice(), product.getId()))
                .toList();
        List<String> photos = products.stream()
                .map(product -> String.format(URL, product.getId()))
                .toList();

        return ProductMenuUI.getProductMenu(
                chatId,
                query,
                descriptions,
                photos
        );
    }

    /***
     *
     * @param chatId
     * @param query query for search product by field, this String may be empty.
     * @param page the number of the page to be retrieved.
     * @param totalPages total pages with products in database.
     * @return SendMessage with navigation buttons.
     */
    private SendMessage openProductMenuButtons(String chatId, String query, int page, int totalPages) {
        return ProductMenuUI.getProductMenuButtons(
                chatId,
                query,
                page - 1,
                page,
                page + 1,
                totalPages
        );
    }

    /***
     *
     * @param callbackQuery
     * @param data contains PRODUCT_(GET_NEXT_PAGE or OPEN_MENU)_(NUMBER_OF_PAGE)_(QUERY) - number of page
     *             always be there, the request may be empty. NUMBER_OF_PAGE contains an integer that indicates
     *             which page needs to be opened. QUERY used for search product, it consists of a field:value.
     * @param chatState used to save the chat state with the user.
     * @return pair of SendMediaGroup and SendMessage, SendMediaGroup contains a list of photos of the products and
     * their description, SendMessage contains the navigation buttons. SendMediaGroup may contain a list of one photo,
     * it needs to check it before executing it. If interceptRequest was called when the button was clicked or the
     * request was invalid, then the media group is created with an empty list.
     */
    public Pair<SendMediaGroup, SendMessage> interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        String chatId = callbackQuery.getMessage().getChatId().toString();

        if (data.contains("OPEN_MENU")) {
            String[] request = data.split("_");
            String query = request.length > 3 ? request[3] : "";

            long count = productService.getProductsCount();
            int totalPages = (int) Math.ceil((double) count / COUNT_OF_PRODUCT_ON_PAGE);
            int size = COUNT_OF_PRODUCT_ON_PAGE <= count ? COUNT_OF_PRODUCT_ON_PAGE : (int) count;

            SendMediaGroup sendMediaGroup = openProductMenu(chatId, query, 0, size);
            SendMessage sendMessage = openProductMenuButtons(chatId, query, 1, totalPages);

            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        } else if (data.contains("GET_PREV_PAGE_") || data.contains("GET_NEXT_PAGE_")) {
            String[] request = data.split("_");
            String query = request.length > 5 ? request[5] : "";
            int page = Integer.parseInt(request[4]);

            long count = productService.getProductsCount();
            int totalPages = (int) Math.ceil((double) count / COUNT_OF_PRODUCT_ON_PAGE);
            int from = (page - 1) * COUNT_OF_PRODUCT_ON_PAGE;
            int size = from + COUNT_OF_PRODUCT_ON_PAGE <= count ? COUNT_OF_PRODUCT_ON_PAGE : (int) (count - from);

            SendMediaGroup sendMediaGroup = openProductMenu(chatId, query, from, size);
            SendMessage sendMessage = openProductMenuButtons(chatId, query, page, totalPages);

            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        } else if (data.contains("SEARCH_BY_FIELD")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_QUERY);
            return new ImmutablePair<>(SendMediaGroup.builder().chatId(chatId).medias(List.of()).build(),
                    SendMessage.builder().chatId(chatId).text("Enter field:value").build());
        }

        return new ImmutablePair<>(
                SendMediaGroup.builder().chatId(chatId).medias(List.of()).build(),
                SendMessage.builder().chatId(chatId).text("Invalid request").build()
        );
    }
    
}
