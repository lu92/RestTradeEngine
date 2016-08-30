package com.tradeengine.TradeEngineAdapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private Long productId;
    private String product;
    private String message;
    private ErrorType errorType;
}
