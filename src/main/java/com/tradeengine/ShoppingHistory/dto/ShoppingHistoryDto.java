package com.tradeengine.ShoppingHistory.dto;

import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistoryDto {
    private Message message;
    private ShoppingHistoryInfo shoppingHistory;
}
