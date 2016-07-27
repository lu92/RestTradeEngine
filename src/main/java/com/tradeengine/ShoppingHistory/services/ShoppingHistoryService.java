package com.tradeengine.ShoppingHistory.services;

import com.tradeengine.ShoppingHistory.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;

public interface ShoppingHistoryService
{
//    ShoppingHistoryDto getShoppingHistory(long customerId);
//
//    ShoppingHistoryDto createShoppingHistory(long customerId);
//
//    ShoppingHistoryDto addOrder(long customerId, Order order);

    ShoppingHistoryDto getShoppingHistory(long customerId);
    ShoppingHistoryDto createShoppingHistory(long customerId);
    ShoppingHistoryDto addOrder(long customerId, CompletedOrder completedOrder);
}
