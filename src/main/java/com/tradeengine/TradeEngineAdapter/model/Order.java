package com.tradeengine.TradeEngineAdapter.model;


import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long customerId;
    private LocalDateTime timeOfSale;
    private List<ProductInfo> productList;
    private Address address;
    private Long gainedPoints;
    private List<Discount> discountList;
    private Price price;

    public List<ProductInfo> getProductList() {
        if (productList == null)
            productList = new ArrayList<>();

        return productList;
    }

    public List<Discount> getDiscountList() {
        if (discountList == null)
            discountList = new ArrayList<>();

        return discountList;
    }
}
