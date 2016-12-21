package com.tradeengine.TradeEngineAdapter.exceptions;


import com.tradeengine.TradeEngineAdapter.model.Order;

public class InvalidBasketException extends Exception {
    private Order order;

    public InvalidBasketException(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
