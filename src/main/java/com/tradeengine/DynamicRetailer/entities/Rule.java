package com.tradeengine.DynamicRetailer.entities;

import com.tradeengine.common.entities.Price;

public class Rule {
    private Long ruleId;
    private RuleType ruleType;
    private Long gainedPoints;
    private Integer discountPercentage;
    private Price oldPrice;
    private Price discountedPrice;
}
