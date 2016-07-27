package com.tradeengine.TradeEngine.services.ProductSelector;

import com.tradeengine.TradeEngine.dto.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductSelector
{
    @Autowired
    private ProductRepository productRepository;

    //    return only active products
    public List<Product> find(ProductCriteria productCriteria)
    {
//        for ()
        return null;
    }
}
