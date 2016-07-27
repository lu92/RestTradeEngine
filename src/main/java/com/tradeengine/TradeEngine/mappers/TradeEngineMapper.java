package com.tradeengine.TradeEngine.mappers;

import com.tradeengine.TradeEngine.dto.CreateProductDto;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import org.springframework.stereotype.Component;

@Component
public class TradeEngineMapper
{
    public Product convertProduct(CreateProductDto createProductDto)
    {
        Product product = new Product();
        product.setCommercialName(createProductDto.getCommercialName());
        product.setProductDescription(createProductDto.getProductDescription());
        product.setAvailable(false);
        product.setQuantity(createProductDto.getQuantity());
        product.setPrice(createProductDto.getPrice());
        product.setImagePath(createProductDto.getImagePath());
        createProductDto.getProductSpecificationList().stream()
                .forEach(ps -> product.getProductSpecificationList().add(
                        ProductSpecification.builder()
                                .property(ps.getProperty())
                                .value(ps.getValue())
                                .unitOfValue(ps.getUnitOfValue())
                                .valueType(ps.getValueType())
                                .build()
                ));
        return product;
    }
}
