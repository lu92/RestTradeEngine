package com.tradeengine.TradeEngineAdapter.model;

import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket {
    private Long customerId;
    private LocalDateTime timeOfSale;
    private String targetCurrency;
    private List<ProductInfo> productList;
    private Address address;

    public List<ProductInfo> getProductList() {
        if (productList == null)
            productList = new ArrayList<>();

        return productList;
    }
}
