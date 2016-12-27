package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor;

import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.CustomerStatusChangedProcessor;
import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.GainedPointProcessor;
import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.PercentPriceDiscountProcessor;
import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.PriceDiscountProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicRetailerConfiguration {

    @Bean
    public PercentPriceDiscountProcessor percentPriceDiscountProcessor() {
        return new PercentPriceDiscountProcessor();
    }

    @Bean
    public PriceDiscountProcessor priceDiscountProcessor() {
        return new PriceDiscountProcessor();
    }

    @Bean
    public GainedPointProcessor gainedPointProcessor() {
        return new GainedPointProcessor();
    }

    @Bean
    public CustomerStatusChangedProcessor customerStatusChangedProcessor()  {
        return new CustomerStatusChangedProcessor();
    }

    @Bean
    public HierarchicalRuleApplier hierarchicalRuleApplier() {
        return new HierarchicalRuleApplier();
    }

    @Bean
    public DynamicRetailerProcessor dynamicRetailerProcessor() {
        return new DynamicRetailerProcessor();
    }

}
