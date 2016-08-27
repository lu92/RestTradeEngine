package com.tradeengine.ShoppingHistory.services;

import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;

public interface ShoppingHistoryService {
    ShoppingHistoryDto getShoppingHistory(long customerId);

    ShoppingHistoryDto createShoppingHistory(long customerId);

    ShoppingHistoryDto addOrder(CreateCompletedOrderDto createCompletedOrderDto);
}
