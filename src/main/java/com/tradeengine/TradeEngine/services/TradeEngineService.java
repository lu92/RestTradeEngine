package com.tradeengine.TradeEngine.services;

import com.tradeengine.TradeEngine.dto.CategoryDto;
import com.tradeengine.TradeEngine.dto.CategoryListDto;
import com.tradeengine.TradeEngine.dto.CreateCategoryDto;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngine.dto.ProductSchemeDto;
import com.tradeengine.TradeEngine.entities.Product;

public interface TradeEngineService
{
    CategoryListDto getCategoryList();
    CategoryDto getCategory(long categoryId);
    ProductSchemeDto getProductSchemeForCategory(long categoryId);

    CategoryDto createCategory(CreateCategoryDto createCategoryDto);
//    Message deleteCategory(long categoryId);


    ProductDto getProduct(long productId);
    ProductListDto getAllProductsForCategory(String categoryName);
    ProductDto addProduct(long categoryId, Product product);
    ProductDto updateProduct(Product product);
    ProductDto activateProduct(long productId);
    ProductDto deactivateProduct(long productId);

    ProductListDto findProducts(ProductCriteria productCriteria);
}
