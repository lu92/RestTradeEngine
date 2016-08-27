package com.tradeengine.ShoppingHistory.dto;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoldProductInfo {
    private Long soldProductId;
    private Long productID; //  to determine proper product
    private int quantity;
    private Price price;
//    private Price totalPrice;
}
