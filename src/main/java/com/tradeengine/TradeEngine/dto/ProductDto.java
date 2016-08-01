package com.tradeengine.TradeEngine.dto;

import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto
{
    private Message message;
    private ProductInfo productInfo;
}
