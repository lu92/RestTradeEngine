package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors;

import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;

public abstract class RuleProcessor {
    public abstract boolean isApply(CustomerInfo customerInfo, RuleDto rule, Order order);
    public abstract Discount calculate(RuleDto rule, Order order);

    protected boolean meetsCriteriasPrice(RuleDto rule, Order order) {
        rule.getAcceptancePriceCriteria().validate();
        double price = order.getPrice().getPrice();
        return lowerOrEqualPriceApplied(rule, price) || greaterOrEqualPriceApplied(rule, price) || equalPriceApplied(rule, price);
    }

    protected boolean meetsTierLevelCriteria(CustomerInfo customerInfo, RuleDto rule) {
        if (rule.getTierLevel() != null) {
            return rule.getTierLevel() == customerInfo.getTierLevel();
        } else return true;
    }

    private boolean lowerOrEqualPriceApplied(RuleDto rule, double price) {
        return rule.getAcceptancePriceCriteria().isStartPriceEmpty()
                && !rule.getAcceptancePriceCriteria().isEndPriceEmpty()
                && price <= rule.getAcceptancePriceCriteria().getEndPrice();
    }

    private boolean greaterOrEqualPriceApplied(RuleDto rule, double price) {
        return !rule.getAcceptancePriceCriteria().isStartPriceEmpty()
                && rule.getAcceptancePriceCriteria().isEndPriceEmpty()
                && price >= rule.getAcceptancePriceCriteria().getStartPrice();
    }

    private boolean equalPriceApplied(RuleDto rule, double price) {
        return !rule.getAcceptancePriceCriteria().isStartPriceEmpty()
                && !rule.getAcceptancePriceCriteria().isEndPriceEmpty()
                && price == rule.getAcceptancePriceCriteria().getStartPrice()
                && price == rule.getAcceptancePriceCriteria().getEndPrice();
    }

}
