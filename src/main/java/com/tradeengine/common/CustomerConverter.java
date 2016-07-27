package com.tradeengine.common;

import com.tradeengine.ProfileReader.entities.Customer;
import org.springframework.core.convert.converter.Converter;

public class CustomerConverter implements Converter<String, Customer>
{
    @Override
    public Customer convert(String s)
    {
        return new Customer();
    }
}
