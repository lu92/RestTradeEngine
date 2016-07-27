package com.tradeengine.TradeEngineAdapter.model.dto;

import com.tradeengine.TradeEngineAdapter.model.Customer;
import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO
{
    private Message message;
    private Customer customer;
}
