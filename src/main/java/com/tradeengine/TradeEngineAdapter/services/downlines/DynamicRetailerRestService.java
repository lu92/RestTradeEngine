package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.DynamicRetailer.dto.RuleListDto;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.services.layers.DynamicRetailerSupportLayer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DynamicRetailerRestService implements DynamicRetailerSupportLayer {

    private RestTemplate restTemplate = new RestTemplate();

    private final int PORT = 8080;
    private final String HOST_ADDRESS = "localhost";
    private final String DYNAMIC_RETAILER_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/DynamicRetailer";


    @Override
    public RuleListDto getRules() {
        return restTemplate.getForObject(DYNAMIC_RETAILER_BASE_URL, RuleListDto.class);
    }

    @Override
    public Order calculateDiscount(Order order) {
        return restTemplate.postForObject(DYNAMIC_RETAILER_BASE_URL, order, Order.class);
    }
}
