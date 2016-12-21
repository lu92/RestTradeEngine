package com.tradeengine.TradeEngineAdapter.exceptions;

import com.tradeengine.TradeEngineAdapter.model.Order;

public class ProductsAvailabilityException extends Exception {
    private Order order;

    public ProductsAvailabilityException(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
