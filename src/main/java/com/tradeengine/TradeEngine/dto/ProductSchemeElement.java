package com.tradeengine.TradeEngine.dto;

import com.tradeengine.TradeEngine.dto.productCriteria.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSchemeElement {
    private String property;
    private ValueType valueType;
    private String unit;
}
