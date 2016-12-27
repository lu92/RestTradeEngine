package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor;


import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.RuleProcessor;
import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DynamicRetailerProcessor {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private List<RuleProcessor> ruleProcessorList;

    @Autowired
    private HierarchicalRuleApplier hierarchicalRuleApplier;

    public Order process(CustomerInfo customerInfo, List<RuleDto> rules, Order order) {
        List<Discount> discounts = new ArrayList<>();
        for (RuleProcessor ruleProcessor : ruleProcessorList) {
            for (RuleDto rule : rules) {
                if (ruleProcessor.isApply(customerInfo, rule, order)) {
                    logger.info("applied RuleProcessor: " + ruleProcessor);
                    discounts.add(ruleProcessor.calculate(rule, order));
                }
            }
        }

        return hierarchicalRuleApplier.applyBestRules(discounts, order);
    }
}
