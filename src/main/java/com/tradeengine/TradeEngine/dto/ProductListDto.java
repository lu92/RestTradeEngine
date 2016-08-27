package com.tradeengine.TradeEngine.dto;

import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto {
    /*
    szukanie produktów względem pewnych kryteriow
    kryteria są związane z ProductDescription pzede wszystkim
     */
    private Message message;
    private String category;
    private List<ProductInfo> productList;

    public List<ProductInfo> getProductList() {
        if (productList == null)
            productList = new ArrayList<>();

        return productList;
    }
}
