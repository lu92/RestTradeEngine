package com.tradeengine.DynamicRetailer.dto;

import com.tradeengine.DynamicRetailer.entities.RuleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleDto {
    private Long ruleId;
    Direct direct;
    RuleType ruleType;
}
