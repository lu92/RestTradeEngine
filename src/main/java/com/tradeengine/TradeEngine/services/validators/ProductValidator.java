package com.tradeengine.TradeEngine.services.validators;

import com.tradeengine.TradeEngine.entities.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors)
    {
        Product product = (Product) object;
    }
}
