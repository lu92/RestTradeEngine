package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.TradeEngineAdapter.services.layers.ShoppingHistorySupportLayer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShoppingHistoryRestService implements ShoppingHistorySupportLayer {

    private RestTemplate restTemplate = new RestTemplate();

    private final int PORT = 8080;
    private final String HOST_ADDRESS = "localhost";
    private final String SHOPPING_HISTORY_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/ShoppingHistory";


    @Override
    public com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto getShoppingHistory(long customerId) {
        return restTemplate.getForObject(SHOPPING_HISTORY_BASE_URL + "/" + customerId, com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto.class);
    }

    @Override
    public ShoppingHistoryDto createShoppingHistory(long customerId) {
        return restTemplate.postForObject(SHOPPING_HISTORY_BASE_URL, customerId, ShoppingHistoryDto.class);
    }

    @Override
    public ShoppingHistoryDto addOrder(CreateCompletedOrderDto createCompletedOrderDto) {
        return restTemplate.postForObject(SHOPPING_HISTORY_BASE_URL + "/Orders", createCompletedOrderDto, ShoppingHistoryDto.class);
    }
}
