package com.tradeengine.TradeEngine.dto;

import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto
{
    private Message message;
    private CategoryInfo categoryInfo;
}
