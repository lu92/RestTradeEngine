package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors;

import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import org.springframework.stereotype.Component;

import static com.tradeengine.DynamicRetailer.entities.RuleType.CUSTOMER_STATUS_CHANGED;

@Component
public class CustomerStatusChangedProcessor extends RuleProcessor {

    @Override
    public boolean isApply(CustomerInfo customerInfo, RuleDto rule, Order order) {
        return rule.getRuleType() == CUSTOMER_STATUS_CHANGED && meetsCriteriasPrice(rule, order);
    }

    @Override
    public Discount calculate(RuleDto rule, Order order) {
        return new Discount(rule.getRuleId(), CUSTOMER_STATUS_CHANGED, null, null, rule.getTierLevel(), order.getPrice(), order.getPrice());
    }
}
