package com.tradeengine.ShoppingHistory;

import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistoryDto
{
    private Message message;
    private ShoppingHistory shoppingHistory;
}
