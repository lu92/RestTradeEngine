package com.tradeengine.TradeEngineAdapter.services.layers;

import com.tradeengine.TradeEngineAdapter.exceptions.*;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Order;

public interface BasketSupportLayer {
    Order createOrder(Basket basket) throws InvalidBasketException;

    Order checkProductsAvailability(Order order) throws ProductsAvailabilityException;

    Order calculateOrder(Order order);

    Order calculateDiscount(Order order);

    Order checkAccountBalance(Order order) throws NotEnoughAccountBalanceException;

    Order updateCustomerStatus(Order order);

    Order updateProductsAvailability(Order order);

    Order processOrder(Order order);

    Order doShopping(Basket basket);
}
