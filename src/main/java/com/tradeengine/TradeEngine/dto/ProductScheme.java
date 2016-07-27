package com.tradeengine.TradeEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductScheme
{
    private String categoryName;

    // Key - property name, Value - property type
    private Map<String, String> basicProductSchema = new HashMap<>();
}
