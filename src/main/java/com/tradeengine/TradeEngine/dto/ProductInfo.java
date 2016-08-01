package com.tradeengine.TradeEngine.dto;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private Long productId;
    private String commercialName;
    private String productDescription;
    private boolean isAvailable;
    private int quantity;
    private Price price;
    private String imagePath;
    private String category;
    private List<ProductSpecification> productSpecificationList;

    public List<ProductSpecification> getProductSpecificationList() {
        if (productSpecificationList == null) {
            productSpecificationList = new ArrayList<>();
        }
        return productSpecificationList;
    }
}
