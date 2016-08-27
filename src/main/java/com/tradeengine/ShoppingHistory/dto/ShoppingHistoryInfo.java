package com.tradeengine.ShoppingHistory.dto;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistoryInfo {
    private Long shoppingHistoryId;
    private Long customerId;
//    private String syntheticId;
    private List<CompletedOrderInfo> completedOrderList;
    private Price spendMoney;


    public List<CompletedOrderInfo> getCompletedOrderList() {
        if (completedOrderList == null) {
            completedOrderList = new ArrayList<>();
        }
        return completedOrderList;
    }
}
