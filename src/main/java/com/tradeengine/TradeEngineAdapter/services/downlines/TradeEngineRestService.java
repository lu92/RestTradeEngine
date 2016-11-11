package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngineAdapter.services.layers.TradeEngineSupportLayer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TradeEngineRestService implements TradeEngineSupportLayer {
    private RestTemplate restTemplate = new RestTemplate();

    private final int PORT = 8080;
    private final String HOST_ADDRESS = "localhost";
    private final String TRADE_ENGINE_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/TradeEngine";
    private final String CATEGORY_BASE_URL = TRADE_ENGINE_BASE_URL + "/Category";
    private final String PRODUCT_BASE_URL = TRADE_ENGINE_BASE_URL + "/Product";


    @Override
    public CategoryListDto getCategoryList() {
        return restTemplate.getForObject(CATEGORY_BASE_URL, CategoryListDto.class);
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        return restTemplate.getForObject(CATEGORY_BASE_URL + "/" + categoryId, CategoryDto.class);
    }

    @Override
    public ProductSchemeDto getProductSchemeForCategory(long categoryId) {
        return restTemplate.getForObject(CATEGORY_BASE_URL + "/GetProductScheme/" + categoryId, ProductSchemeDto.class);
    }

    @Override
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        return restTemplate.postForObject(CATEGORY_BASE_URL, createCategoryDto, CategoryDto.class);
    }

    @Override
    public ProductDto getProduct(long productId) {
        return restTemplate.getForObject(PRODUCT_BASE_URL + "/" + productId, ProductDto.class);
    }

    @Override
    public ProductListDto getProductList(RequestedProductsDto requestedProductsDto) {
        return restTemplate.postForObject(PRODUCT_BASE_URL + "/GetRequestedProducts", requestedProductsDto, ProductListDto.class);
    }

    @Override
    public ProductListDto getAllProductsForCategory(String categoryName) {
        return restTemplate.getForObject(PRODUCT_BASE_URL + "/Find/" + categoryName, ProductListDto.class);
    }

    @Override
    public ProductDto addProduct(CreateProductDto createProductDto) {
        return restTemplate.postForObject(PRODUCT_BASE_URL, createProductDto, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Product product) {
        // TODO change product parameter to ProductDTO
        return null;
    }

    @Override
    public ProductDto updateProductQuantity(long productId, int quantity) {
        return restTemplate.postForObject(PRODUCT_BASE_URL + "/Quantity/" +productId, quantity, ProductDto.class);
    }

    @Override
    public ProductDto activateProduct(long productId) {
        return restTemplate.getForObject(PRODUCT_BASE_URL + "/Activate/" + productId, ProductDto.class);
    }

    @Override
    public ProductDto deactivateProduct(long productId) {
        return restTemplate.getForObject(PRODUCT_BASE_URL + "/Deactivate/" + productId, ProductDto.class);
    }

    @Override
    public ProductListDto findProducts(ProductCriteria productCriteria) {
        return restTemplate.postForObject(PRODUCT_BASE_URL + "/FindByCriteria", productCriteria, ProductListDto.class);
    }
}
