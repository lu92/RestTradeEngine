package com.tradeengine.TradeEngineAdapter.services.layers;

import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Order;

public abstract class BasketSupportLayer {
    protected abstract Order createOrder(Basket basket);

    protected abstract Order checkProductsAvailability(Order order);

    protected abstract Order calculateOrder(Order order);

    protected abstract Order calculateDiscount(Order order);

    protected abstract Order updateCustomerStatus(Order order);

    protected abstract Order updateProductsAvailability(Order order);

    protected abstract Order processOrder(Order order);

    public Order doShopping(Basket basket) {
        Order order = createOrder(basket);
        checkProductsAvailability(order);
        if (order.getFlowResults().isEmpty()) {
            calculateOrder(order);
            calculateDiscount(order);
            updateProductsAvailability(order);
            updateCustomerStatus(order);
            processOrder(order);
        }
        return order;
    }
}
