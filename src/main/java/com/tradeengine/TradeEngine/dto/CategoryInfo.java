package com.tradeengine.TradeEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfo {
    private Long categoryId;
    private String parentCategory;
    private String name;
    private List<String> subCategories;
    private String productSchemaJsonFigure;
    private List<ProductInfo> productList;

    public List<String> getSubCategories() {
        if (subCategories == null) {
            subCategories = new ArrayList<>();
        }
        return subCategories;
    }

    public List<ProductInfo> getProductList() {
        if (productList == null) {
            productList = new ArrayList<>();
        }
        return productList;
    }
}
