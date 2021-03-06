package com.tradeengine.TradeEngineAdapter.services.adapter;


import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;

public interface Adapter {
    CustomerDto createCustomerAndHisShoppingHistory(CreateCustomerDto createCustomerDto);
    CustomerDTO getCustomerWithShoppingHistory(long customerId);
    Order doShopping(Basket basket);
}
