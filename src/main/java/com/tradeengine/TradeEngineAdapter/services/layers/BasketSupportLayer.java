package com.tradeengine.TradeEngineAdapter.services.layers;

import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;

public interface BasketSupportLayer {
    boolean checkProductsAvailability(Basket basket);

    Order calculateOrder(Basket basket);

    Order calculatePoints(Order order);

    Order calculateDiscount(Order order);

    Order updateProductsAvailability(Order order);

    void processOrder(Order order);
}
