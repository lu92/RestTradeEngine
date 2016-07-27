package com.tradeengine.TradeEngineAdapter.model;

import com.tradeengine.TradeEngine.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket
{
    private Long custimerId;
    private LocalDateTime timeOfSale;
    private List<Product> productList;
    private Price price;
}
