package com.tradeengine.ProfileReader;

import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto
{
    private Message message;
    private Customer customer;
}
