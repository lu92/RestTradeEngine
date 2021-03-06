package com.tradeengine.TradeEngine.dto;

import com.tradeengine.TradeEngine.dto.productCriteria.ValueType;
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
    private ValueType valueType;
}
