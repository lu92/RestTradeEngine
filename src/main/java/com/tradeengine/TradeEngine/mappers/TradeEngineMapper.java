package com.tradeengine.TradeEngine.mappers;

import com.tradeengine.TradeEngine.dto.CategoryInfo;
import com.tradeengine.TradeEngine.dto.CreateProductDto;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.TradeEngine.entities.Category;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import com.tradeengine.common.entities.Price;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TradeEngineMapper {

    public Product convertProduct(CreateProductDto createProductDto) {
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

    public CategoryInfo convertCategory(Category category) {
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

        if (!category.getProductList().isEmpty()) {
            categoryInfo.getProductList().addAll(
                    category.getProductList().stream().map(product -> convertProduct(product))
                            .collect(Collectors.toList())
            );
        }

        return categoryInfo;
    }

    public List<ProductInfo> convertProductList(List<Product> productList) {
        List<ProductInfo> productInfoList = productList.stream().map(product -> convertProduct(product)).collect(Collectors.toList());
        return productInfoList;
    }

    public ProductInfo convertProduct(Product product) {
        ProductInfo productInfo = new ProductInfo();

        if (product.getProductId() != null)
            productInfo.setProductId(product.getProductId());

        if (product.getCommercialName() != null)
            productInfo.setCommercialName(product.getCommercialName());

        if (product.getProductDescription() != null)
            productInfo.setProductDescription(product.getProductDescription());

        productInfo.setAvailable(product.isAvailable());

        productInfo.setQuantity(product.getQuantity());

        if (product.getPrice() != null)
            productInfo.setPrice(Price.builder()
                    .amount(product.getPrice().getAmount())
                    .tax(product.getPrice().getTax())
                    .price(product.getPrice().getPrice())
                    .currency(product.getPrice().getCurrency())
                    .build());

        if (product.getImagePath() != null)
            productInfo.setImagePath(product.getImagePath());

        if (product.getCategory() != null)
            productInfo.setCategory(product.getCategory().getName());

        if (!product.getProductSpecificationList().isEmpty()) {
            for (ProductSpecification productSpecification : product.getProductSpecificationList()) {
                productInfo.getProductSpecificationList().add(
                        com.tradeengine.TradeEngine.dto.ProductSpecification.builder()
                                .property(productSpecification.getProperty())
                                .value(productSpecification.getValue())
                                .unitOfValue(productSpecification.getUnitOfValue())
                                .valueType(productSpecification.getValueType())
                                .build()
                );
            }
        }

        return productInfo;
    }
}
