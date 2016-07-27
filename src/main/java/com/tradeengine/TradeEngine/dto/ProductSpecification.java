package com.tradeengine.TradeEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecification
{
    private String property;
    private String value;
    private String unitOfValue;
    private String valueType;
}
