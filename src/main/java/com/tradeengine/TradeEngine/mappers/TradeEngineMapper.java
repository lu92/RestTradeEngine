package com.tradeengine.TradeEngine.mappers;

import com.tradeengine.TradeEngine.dto.CategoryInfo;
import com.tradeengine.TradeEngine.dto.CreateProductDto;
import com.tradeengine.TradeEngine.entities.Category;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public CategoryInfo convertCategory(Category category)
    {
        CategoryInfo categoryInfo = new CategoryInfo();

        if (category.getCategoryId() != null)
            categoryInfo.setCategoryId(category.getCategoryId());

        if (category.getName() != null)
            categoryInfo.setName(category.getName());

        if (category.getParent() != null && category.getParent().getName() != null)
            categoryInfo.setParentCategory(category.getParent().getName());

        if (!category.getSubCategories().isEmpty())
            categoryInfo.setSubCategories(category.getSubCategories().stream().map(Category::getName).collect(Collectors.toList()));
        else categoryInfo.setSubCategories(new ArrayList<>());

        if (category.getProductSchemaJsonFigure() != null)
            categoryInfo.setProductSchemaJsonFigure(category.getProductSchemaJsonFigure());

        return categoryInfo;
    }
}
