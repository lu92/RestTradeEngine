package com.tradeengine.TradeEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryElement {
    private Long categoryId;
    private String category;
    private String productSchemaJsonFigure;
}
