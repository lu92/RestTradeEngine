package com.tradeengine.TradeEngine.controllers;

import com.tradeengine.TradeEngine.dto.CategoryDto;
import com.tradeengine.TradeEngine.dto.CategoryListDto;
import com.tradeengine.TradeEngine.dto.CreateCategoryDto;
import com.tradeengine.TradeEngine.dto.CreateProductDto;
import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngine.dto.ProductSchemeDto;
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

import java.util.logging.Logger;

@RestController
@RequestMapping("/TradeEngine")
public class TradeEngineController
{
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired(required = false)
    private TradeEngineServiceImpl tradeEngineService;

    @Autowired
    private TradeEngineMapper tradeEngineMapper;

    @RequestMapping(value = "/Category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public CategoryDto getCategory(@PathVariable(value = "categoryId") long id)
    {
        return tradeEngineService.getCategory(id);
    }

    @RequestMapping(value = "/Category", method = RequestMethod.GET)
    @ResponseBody
    public CategoryListDto getCategoryList()
    {
        return tradeEngineService.getCategoryList();
    }

    @RequestMapping(value = "/Category", method = RequestMethod.POST)
    @ResponseBody
    public CategoryDto createCategory(@RequestBody CreateCategoryDto createCategoryDto)
    {
        return tradeEngineService.createCategory(createCategoryDto);
    }

    @RequestMapping(value = "/Category/GetProductScheme/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductSchemeDto getProductSchemeForCategory(@PathVariable(value = "categoryId") long id)
    {
        return tradeEngineService.getProductSchemeForCategory(id);
    }

    //    @RequestMapping(value = "/Category/{id}", method = RequestMethod.DELETE)
    //    @ResponseBody
    //    public Message deleteCategory(@PathVariable(value = "id") long id)
    //    {
    //        return tradeEngineService.deleteCategory(id);
    //    }
    //
    //    @RequestMapping(value = "/Product/{id}", method = RequestMethod.GET)
    //    @ResponseBody
    //    public Product getProduct(@PathVariable(value = "id") long id)
    //    {
    //        return tradeEngineService.getProduct(id);
    //    }
    //
    @RequestMapping(value = "/Product/{categoryName}", method = RequestMethod.GET)
    @ResponseBody
    public ProductListDto getProductList(@PathVariable("categoryName") String categoryName)
    {
        return tradeEngineService.getAllProductsForCategory(categoryName);
    }

    @RequestMapping(value = "/Product", method = RequestMethod.POST)
    @ResponseBody
    public ProductDto addProduct(@RequestBody CreateProductDto createProductDto)
    {
        return tradeEngineService.addProduct(createProductDto.getCategoryId(), tradeEngineMapper.convertProduct(createProductDto));
    }

    @RequestMapping(value = "/Product", method = RequestMethod.PUT)
    @ResponseBody
    public ProductDto updateProduct(@RequestBody Product product)
    {
        return tradeEngineService.updateProduct(product);
    }

    //    @RequestMapping(value = "/Product/{id}", method = RequestMethod.DELETE)
    //    @ResponseBody
    //    public Message deleteProduct(@PathVariable(value = "id") long id)
    //    {
    //        return tradeEngineService.deleteProduct(id);
    //    }
}
