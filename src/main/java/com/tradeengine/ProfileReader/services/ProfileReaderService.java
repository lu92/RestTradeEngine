package com.tradeengine.ProfileReader.services;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.common.Message;

import java.util.List;

public interface ProfileReaderService
{
    CustomerDto getCustomer(long customerId);
    CustomerDtoList getCustomerList();
    CustomerDto deleteCustomer(long customerId);
    CustomerDto createCustomer(CreateCustomerDto createCustomerDto);
    CustomerDto updateCustomer(Customer customer);
    CustomerDto login(String username, String password);
}
