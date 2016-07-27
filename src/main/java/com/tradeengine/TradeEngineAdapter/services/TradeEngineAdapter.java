package com.tradeengine.TradeEngineAdapter.services;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.layers.CustomerSupportLayer;
import com.tradeengine.TradeEngineAdapter.services.processors.BasketProcessor;
import com.tradeengine.TradeEngineAdapter.services.processors.CustomerProcessor;
import com.tradeengine.TradeEngineAdapter.services.processors.TradeEngineProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeEngineAdapter implements /*BasketSupportLayer,*/ CustomerSupportLayer//, TradeEngineSupportLayer2
{
    @Autowired
    private BasketProcessor basketProcessor;

    @Autowired
    private CustomerProcessor customerProcessor;

    @Autowired
    private TradeEngineProcessor tradeEngineProcessor;

    @Override
    public CustomerDto getCustomer(long customerId)
    {
        return customerProcessor.getCustomer(customerId);
    }

    @Override
    public CustomerDtoList getCustomerList()
    {
        return customerProcessor.getCustomerList();
    }

    @Override
    public CustomerDto deleteCustomer(long customerId)
    {
        return customerProcessor.deleteCustomer(customerId);
    }

//    @Override
//    public CustomerDTO createCustomer(CreateCustomerDto customer)
//    {
//        return customerProcessor.createCustomer(customer);
//    }

    @Override public CustomerDto createCustomer(CreateCustomerDto createCustomerDto)
    {
        return null;
    }

    @Override
    public CustomerDto updateCustomer(com.tradeengine.ProfileReader.entities.Customer customer)
    {
        return customerProcessor.updateCustomer(customer);
    }

    @Override public CustomerDto login(String username, String password)
    {
        return null;
    }

    @Override
    public CustomerDTO getCustomerWithShoppingHistory(long customerId)
    {
        return customerProcessor.getCustomerWithShoppingHistory(customerId);
    }
}
