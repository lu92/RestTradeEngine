package com.tradeengine.TradeEngine.services.ProductSelector;

import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecificationComparator
{
    public boolean compare(Product product, List<ProductSpecification> productSpecificationList)
    {
        return false;
    }
}
