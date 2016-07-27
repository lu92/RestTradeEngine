package com.tradeengine.TradeEngineAdapter.services.layers;

import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngine.services.TradeEngineService;

import java.util.List;

public interface TradeEngineSupportLayer2 extends TradeEngineService
{
    List<ProductDto> findProductByName(String name);
}
