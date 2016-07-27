package com.tradeengine.TradeEngineAdapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price
{
    private double amount;
    private double tax;
    private double price;
    private String currency;
}
