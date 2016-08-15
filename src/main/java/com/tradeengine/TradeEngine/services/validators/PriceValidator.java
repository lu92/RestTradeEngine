package com.tradeengine.TradeEngine.services.validators;

import com.tradeengine.common.entities.Price;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PriceValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return Price.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "amount.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tax", "tax.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cost", "cost.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currency", "currency.required");
        Price price = (Price) object;

        if (price.getAmount() <= 0)
            errors.rejectValue("amount", "negativeValue", new Object[]{"'amount'"}, "amount can't be zero or negative");

        if (price.getTax() < 0)
            errors.rejectValue("tax", "negativeValue", new Object[]{"'tax'"}, "tax can't be negative");

        if (price.getPrice() <= 0)
            errors.rejectValue("cost", "negativeValue", new Object[]{"'cost'"}, "cost can't be zero or negative");

        if (price.getAmount() + price.getTax() != price.getPrice())
            errors.rejectValue("cost", "negativeValue", new Object[]{"'cost'"}, "cost doesn't equal to sum of amount and tax");
    }
}
