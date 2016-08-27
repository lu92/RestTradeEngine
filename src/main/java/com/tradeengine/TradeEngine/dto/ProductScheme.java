package com.tradeengine.TradeEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductScheme
{
    private String categoryName;

//    // Key - property name, Value - property type
//    private Map<String, String> basicProductSchema = new HashMap<>();
    private List<ProductSchemeElement> productSchemeElements;

    public List<ProductSchemeElement> getProductSchemeElements() {
        if (productSchemeElements == null)
            productSchemeElements = new ArrayList<>();
        return productSchemeElements;
    }
}
