package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors;

import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.common.entities.Price;
import org.springframework.stereotype.Component;

import static com.tradeengine.DynamicRetailer.entities.RuleType.AWARDED_PRICE_DISCOUNT;

@Component
public class PriceDiscountProcessor extends RuleProcessor {

    @Override
    public boolean isApply(CustomerInfo customerInfo, RuleDto rule, Order order) {
        return rule.getRuleType() == AWARDED_PRICE_DISCOUNT
                && meetsCriteriasPrice(rule, order)
                && meetsTierLevelCriteria(customerInfo, rule);
    }

    @Override
    public Discount calculate(RuleDto rule, Order order) {
        Price discountedPrice = new Price().builder()
                .amount(order.getPrice().getAmount() - rule.getDiscount())
                .tax(order.getPrice().getTax())
                .price(order.getPrice().getPrice() - rule.getDiscount())
                .currency(order.getPrice().getCurrency())
                .build();

        return new Discount(rule.getRuleId(), AWARDED_PRICE_DISCOUNT, null, null, null, order.getPrice(), discountedPrice);
    }
}
