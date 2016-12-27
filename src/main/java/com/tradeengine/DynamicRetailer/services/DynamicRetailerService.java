package com.tradeengine.DynamicRetailer.services;

import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.DynamicRetailer.dto.RuleListDto;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;

public interface DynamicRetailerService
{
//    RuleDto addRule(RuleDto ruleDto);
//    RuleListDto getRuleList();
//    RuleDto getRule(long ruleId);
//    Discount calculateDiscount(Order order);
//    long calculatePoints(Order order);
//    boolean activateRule(long ruleId);
//    boolean deactivateRule(long ruleId);

    RuleListDto getRules();
    Order calculateDiscount(Order order);
}
