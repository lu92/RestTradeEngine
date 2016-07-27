package com.tradeengine.TradeEngineAdapter.controllers;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.TradeEngineAdapter;
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
        return tradeEngineAdapter.createCustomer(customer);
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
    public CustomerDto loginCustomer(@PathVariable("username") String username, @PathVariable("password") String password)
    {
        return tradeEngineAdapter.login(username, password);
    }

    @RequestMapping(value = "/ProfileReader/GetShoppingHistory/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDTO getCustogetCustomerWithShoppingHistory(@PathVariable("customerId") long customerId)
    {
        return tradeEngineAdapter.getCustomerWithShoppingHistory(customerId);
    }
}
