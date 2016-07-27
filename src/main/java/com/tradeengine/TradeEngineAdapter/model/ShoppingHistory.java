package com.tradeengine.TradeEngineAdapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Builder;

import java.util.List;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistory
{
    private List<Order> orderList;
    private Price totalPrice;
}
