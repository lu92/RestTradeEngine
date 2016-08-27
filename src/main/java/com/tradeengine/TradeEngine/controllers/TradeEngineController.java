package com.tradeengine.TradeEngine.controllers;

import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.mappers.TradeEngineMapper;
import com.tradeengine.TradeEngine.services.TradeEngineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/TradeEngine")
public class TradeEngineController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired(required = false)
    private TradeEngineServiceImpl tradeEngineService;

    @Autowired
    private TradeEngineMapper tradeEngineMapper;

    @RequestMapping(value = "/Category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public CategoryDto getCategory(@PathVariable(value = "categoryId") long id) {
        return tradeEngineService.getCategory(id);
    }

    @RequestMapping(value = "/Category", method = RequestMethod.GET)
    @ResponseBody
    public CategoryListDto getCategoryList() {
        return tradeEngineService.getCategoryList();
    }

    @RequestMapping(value = "/Category", method = RequestMethod.POST)
    @ResponseBody
    public CategoryDto createCategory(@RequestBody CreateCategoryDto createCategoryDto) {
        return tradeEngineService.createCategory(createCategoryDto);
    }

    @RequestMapping(value = "/Category/GetProductScheme/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductSchemeDto getProductSchemeForCategory(@PathVariable(value = "categoryId") long id) {
        return tradeEngineService.getProductSchemeForCategory(id);
    }

    //    @RequestMapping(value = "/Category/{id}", method = RequestMethod.DELETE)
    //    @ResponseBody
    //    public Message deleteCategory(@PathVariable(value = "id") long id)
    //    {
    //        return tradeEngineService.deleteCategory(id);
    //    }
    //
    @RequestMapping(value = "/Product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ProductDto getProduct(@PathVariable(value = "id") long id) {
        return tradeEngineService.getProduct(id);
    }

    @RequestMapping(value = "/Product/GetRequestedProducts", method = RequestMethod.POST)
    @ResponseBody
    public ProductListDto getRequestedProductList(@RequestBody RequestedProductsDto productList) {
        return tradeEngineService.getProductList(productList);
    }

    @RequestMapping(value = "/Product/Find/{categoryName}", method = RequestMethod.GET)
    @ResponseBody
    public ProductListDto getProductList(@PathVariable("categoryName") String categoryName) {
        return tradeEngineService.getAllProductsForCategory(categoryName);
    }

    @RequestMapping(value = "/Product", method = RequestMethod.POST)
    @ResponseBody
    public ProductDto addProduct(@RequestBody CreateProductDto createProductDto) {
//        return tradeEngineService.addProduct(createProductDto.getCategoryId(), tradeEngineMapper.convertProduct(createProductDto));
        return tradeEngineService.addProduct(createProductDto);
    }

    @RequestMapping(value = "/Product", method = RequestMethod.PUT)
    @ResponseBody
    public ProductDto updateProduct(@RequestBody Product product) {
        return tradeEngineService.updateProduct(product);
    }

    //    @RequestMapping(value = "/Product/{id}", method = RequestMethod.DELETE)
    //    @ResponseBody
    //    public Message deleteProduct(@PathVariable(value = "id") long id)
    //    {
    //        return tradeEngineService.deleteProduct(id);
    //    }

    @RequestMapping(value = "/Product/Activate/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductDto activateProduct(@PathVariable(value = "productId") long productId) {
        return tradeEngineService.activateProduct(productId);
    }

    @RequestMapping(value = "/Product/Deactivate/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductDto deactivateProduct(@PathVariable(value = "productId") long productId) {
        return tradeEngineService.deactivateProduct(productId);
    }

    @RequestMapping(value = "/Product/FindByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public ProductListDto findProductsByCriteria(@RequestBody ProductCriteria productCriteria) {
        return tradeEngineService.findProducts(productCriteria);
    }
}
