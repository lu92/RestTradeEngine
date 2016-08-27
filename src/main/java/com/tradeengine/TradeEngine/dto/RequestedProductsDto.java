package com.tradeengine.TradeEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestedProductsDto {
    private List<Long> requestedProducts;

    public List<Long> getRequestedProducts() {
        if (requestedProducts == null)
            requestedProducts = new ArrayList<>();

        return requestedProducts;
    }
}
