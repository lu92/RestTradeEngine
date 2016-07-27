package com.tradeengine.TradeEngineAdapter.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order
{
    private Long orderId;
    private LocalDateTime timeOfSale;
    private List<Product> productList;
    private Price price;
}
