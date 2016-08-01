package com.tradeengine.TradeEngine.dto;

import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto
{
    private Message message;
    private String category;
    private long amountOfProducts;
    private List<ProductInfo> productList;
}
