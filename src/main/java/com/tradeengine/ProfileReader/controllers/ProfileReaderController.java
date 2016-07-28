package com.tradeengine.ProfileReader.controllers;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.LoginDto;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.ProfileReader.services.ProfileReaderServiceImpl;
import com.tradeengine.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ProfileReader")
public class ProfileReaderController
{
    @Autowired(required = false)
    private ProfileReaderServiceImpl profileReaderService;

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDto getCustomer(@PathVariable("customerId") long customerId)
    {
        return profileReaderService.getCustomer(customerId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public CustomerDtoList getCustomerList()
    {
        return profileReaderService.getCustomerList();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public CustomerDto createCustomer(@RequestBody CreateCustomerDto createCustomerDto)
    {
        return profileReaderService.createCustomer(createCustomerDto);
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.DELETE)
    @ResponseBody
    public CustomerDto deleteCustomer(@PathVariable("customerId") long customerId)
    {
        return profileReaderService.deleteCustomer(customerId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public CustomerDto updateCustomer(@RequestBody Customer customer)
    {
        return profileReaderService.updateCustomer(customer);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CustomerDto loginCustomer(@RequestBody LoginDto loginDto)
    {
        return profileReaderService.login(loginDto.getUsername(), loginDto.getPassword());
    }
}
