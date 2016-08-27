package com.tradeengine.TradeEngine.services;

import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Product;

import java.util.List;

public interface TradeEngineService
{
    CategoryListDto getCategoryList();
    CategoryDto getCategory(long categoryId);
    ProductSchemeDto getProductSchemeForCategory(long categoryId);

    CategoryDto createCategory(CreateCategoryDto createCategoryDto);
//    Message deleteCategory(long categoryId);


    ProductDto getProduct(long productId);
    ProductListDto getProductList(RequestedProductsDto requestedProductsDto);
    ProductListDto getAllProductsForCategory(String categoryName);
//    ProductDto addProduct(long categoryId, Product product);
    ProductDto addProduct(CreateProductDto createProductDto);
    ProductDto updateProduct(Product product);
    ProductDto activateProduct(long productId);
    ProductDto deactivateProduct(long productId);

    ProductListDto findProducts(ProductCriteria productCriteria);
}
