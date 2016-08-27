package com.tradeengine.DynamicRetailer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleListDto {
    List<RuleDto> ruleDtoList;
}
