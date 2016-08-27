package com.tradeengine.TradeEngineAdapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import com.tradeengine.common.entities.Price;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
    private Long productId;
    private String commercialName;
    private String productDescription;
    private String category;
    private boolean isAvailable;
    private int quantity;
    private Price price;
    private String imagePath;
    private List<ProductSpecification> productSpecificationList;
}
