package com.tradeengine.TradeEngine.dto;

import com.tradeengine.TradeEngine.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfo
{
    private Long categoryId;
    private String parentCategory;
    private String name;
    private List<String> subCategories;
    private String productSchemaJsonFigure;
    private List<Product> productList;
}
