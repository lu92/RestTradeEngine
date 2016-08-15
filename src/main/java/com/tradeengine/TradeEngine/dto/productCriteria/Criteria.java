package com.tradeengine.TradeEngine.dto.productCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Criteria
{
    private String property;
    private String value;
    private String unitOfValue;
    private ValueType valueType;
    private Direct direct;
}
