package com.tradeengine.TradeEngineAdapter.controllers;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.LoginDto;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.TradeEngine.dto.CategoryDto;
import com.tradeengine.TradeEngine.dto.CategoryListDto;
import com.tradeengine.TradeEngine.dto.CreateCategoryDto;
import com.tradeengine.TradeEngine.dto.ProductSchemeDto;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.adapter.TradeEngineAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/TradeEngineAdapter")
@RestController
public class TradeEngineAdapterController
{
    @Autowired(required = false)
    private TradeEngineAdapter tradeEngineAdapter;

    @RequestMapping(value = "/ProfileReader/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDto getCustomer(@PathVariable("customerId") long customerId)
    {
        return tradeEngineAdapter.getCustomer(customerId);
    }

    @RequestMapping(value = "/ProfileReader", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDtoList getCustomerList()
    {
        return tradeEngineAdapter.getCustomerList();
    }

    @RequestMapping(value = "/ProfileReader", method = RequestMethod.POST)
    @ResponseBody
    public CustomerDto createCustomer(@RequestBody CreateCustomerDto customer)
    {
        return tradeEngineAdapter.createCustomerAndHisShoppingHistory(customer);
    }

    @RequestMapping(value = "/ProfileReader/{customerId}", method = RequestMethod.DELETE)
    @ResponseBody
    public CustomerDto deleteCustomer(@PathVariable("customerId") long customerId)
    {
        return tradeEngineAdapter.deleteCustomer(customerId);
    }

    // up ok

    @RequestMapping(value = "/ProfileReader", method = RequestMethod.PUT)
    @ResponseBody
    public CustomerDto updateCustomer(@RequestBody Customer customer)
    {
        return tradeEngineAdapter.updateCustomer(customer);
    }

    @RequestMapping(value = "/ProfileReader/login", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDto loginCustomer(@RequestBody LoginDto loginDto)
    {
        return tradeEngineAdapter.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @RequestMapping(value = "/ProfileReader/GetShoppingHistory/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDTO getCustomerWithShoppingHistory(@PathVariable("customerId") long customerId)
    {
        CustomerDTO customerWithShoppingHistory = tradeEngineAdapter.getCustomerWithShoppingHistory(customerId);
        return customerWithShoppingHistory;
    }

    @RequestMapping(value = "/Category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public CategoryDto getCategory(@PathVariable(value = "categoryId") long id) {
        return tradeEngineAdapter.getCategory(id);
    }

    @RequestMapping(value = "/Category", method = RequestMethod.GET)
    @ResponseBody
    public CategoryListDto getCategoryList() {
        return tradeEngineAdapter.getCategoryList();
    }

    @RequestMapping(value = "/Category", method = RequestMethod.POST)
    @ResponseBody
    public CategoryDto createCategory(@RequestBody CreateCategoryDto createCategoryDto) {
        return tradeEngineAdapter.createCategory(createCategoryDto);
    }

    @RequestMapping(value = "/Category/GetProductScheme/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductSchemeDto getProductSchemeForCategory(@PathVariable(value = "categoryId") long id) {
        return tradeEngineAdapter.getProductSchemeForCategory(id);
    }
}
