package com.tradeengine.TradeEngineAdapter.model;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistory {
    private List<Order> orderList;
    private Price totalPrice;

    public List<Order> getOrderList() {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        return orderList;
    }
}
