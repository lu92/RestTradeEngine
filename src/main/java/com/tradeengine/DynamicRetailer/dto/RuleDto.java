package com.tradeengine.DynamicRetailer.dto;

import com.tradeengine.DynamicRetailer.entities.RuleType;
import com.tradeengine.ProfileReader.entities.TierLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tradeengine.DynamicRetailer.entities.RuleType.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleDto {
    private Long ruleId;
    private Integer gainedPoints;
    private Double discount;
    private Double percentDiscount;
    private TierLevel tierLevel;
    private AcceptancePriceCriteria acceptancePriceCriteria;
    private RuleType ruleType;

    public static RuleDto gainedPointsRule(Integer gainedPoints, AcceptancePriceCriteria acceptancePriceCriteria) {
        return new RuleDto(null, gainedPoints, null, null, null, acceptancePriceCriteria, GAINED_POINTS);
    }

    public static RuleDto priceDiscountRule(Double discount, AcceptancePriceCriteria acceptancePriceCriteria) {
        return new RuleDto(null, null, discount,null, null, acceptancePriceCriteria, AWARDED_PRICE_DISCOUNT);
    }

    public static RuleDto priceDiscountRule(TierLevel tierLevel, Double discount, AcceptancePriceCriteria acceptancePriceCriteria) {
        return new RuleDto(null, null, discount,null, tierLevel, acceptancePriceCriteria, AWARDED_PRICE_DISCOUNT);
    }

    public static RuleDto percentPriceDiscountRule(Double percentDiscount, AcceptancePriceCriteria acceptancePriceCriteria) {
        return new RuleDto(null, null, null,percentDiscount, null, acceptancePriceCriteria, AWARDED_PERCENT_PRICE_DISCOUNT);
    }

    public static RuleDto percentPriceDiscountRule(TierLevel tierLevel, Double percentDiscount, AcceptancePriceCriteria acceptancePriceCriteria) {
        return new RuleDto(null, null, null,percentDiscount, tierLevel, acceptancePriceCriteria, AWARDED_PERCENT_PRICE_DISCOUNT);
    }

    public static RuleDto customerStatusChangedRule(TierLevel tierLevel, AcceptancePriceCriteria acceptancePriceCriteria) {
        return new RuleDto(null, null, null,null, tierLevel, acceptancePriceCriteria, CUSTOMER_STATUS_CHANGED);
    }
}
