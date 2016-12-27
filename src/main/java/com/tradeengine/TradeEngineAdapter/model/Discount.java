package com.tradeengine.TradeEngineAdapter.model;

import com.tradeengine.DynamicRetailer.entities.RuleType;
import com.tradeengine.ProfileReader.entities.TierLevel;
import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    private Long ruleId;
    private RuleType ruleType;
    private Long productId;
    private Integer gainedPoints;
//    private Integer discountPercentage;
    private TierLevel upgradedTierLevel;
    private Price oldPrice;
    private Price discountedPrice;
}
