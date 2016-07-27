package com.tradeengine.TradeEngine.services.validators;

import com.tradeengine.TradeEngine.dto.ProductScheme;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Map;

@Component
public class ProductSchemeValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return ProductScheme.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors)
    {
        ValidationUtils.rejectIfEmpty(errors, "", ".empty");
        ProductScheme productScheme = (ProductScheme) object;
        for (Map.Entry<String, String> entry : productScheme.getBasicProductSchema().entrySet())
        {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, entry.getValue(), entry.getKey() + ".required");
        }
    }
}
