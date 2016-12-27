package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors;

import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import org.springframework.stereotype.Component;

import static com.tradeengine.DynamicRetailer.entities.RuleType.AWARDED_PERCENT_PRICE_DISCOUNT;
import static com.tradeengine.DynamicRetailer.entities.RuleType.GAINED_POINTS;

@Component
public class GainedPointProcessor extends RuleProcessor {


    @Override
    public boolean isApply(CustomerInfo customerInfo, RuleDto rule, Order order) {
        return rule.getRuleType() == GAINED_POINTS
                && meetsCriteriasPrice(rule, order)
                && meetsTierLevelCriteria(customerInfo, rule);
    }

    @Override
    public Discount calculate(RuleDto rule, Order order) {
        return new Discount(rule.getRuleId(), GAINED_POINTS, null, rule.getGainedPoints(), null, order.getPrice(), order.getPrice());
    }
}
