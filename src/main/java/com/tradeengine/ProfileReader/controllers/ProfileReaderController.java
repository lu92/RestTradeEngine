package com.tradeengine.ProfileReader.controllers;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.LoginDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.ProfileReader.services.ProfileReaderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ProfileReader")
public class ProfileReaderController {
    @Autowired(required = false)
    private ProfileReaderServiceImpl profileReaderService;

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public CustomerDto getCustomer(@PathVariable("customerId") long customerId) {
        return profileReaderService.getCustomer(customerId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public CustomerDtoList getCustomerList() {
        return profileReaderService.getCustomerList();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public CustomerDto createCustomer(@RequestBody CreateCustomerDto createCustomerDto) {
        return profileReaderService.createCustomer(createCustomerDto);
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.DELETE)
    @ResponseBody
    public CustomerDto deleteCustomer(@PathVariable("customerId") long customerId) {
        return profileReaderService.deleteCustomer(customerId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public CustomerDto updateCustomer(@RequestBody CustomerInfo customer) {
        return profileReaderService.updateCustomer(customer);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CustomerDto loginCustomer(@RequestBody LoginDto loginDto) {
        return profileReaderService.login(loginDto.getUsername(), loginDto.getPassword());
    }
}
