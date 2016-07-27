package com.tradeengine.TradeEngineAdapter.services.layers;

import com.tradeengine.ProfileReader.services.ProfileReaderService;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;

public interface CustomerSupportLayer extends ProfileReaderService
{
    CustomerDTO getCustomerWithShoppingHistory(long customerId);
}
