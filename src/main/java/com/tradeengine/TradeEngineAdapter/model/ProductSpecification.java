package com.tradeengine.TradeEngineAdapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecification
{
    private String property;
    private String value;
    private String unitOfValue;
    private String valueType;
}
