package com.tradeengine.ProfileReader;

import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Message message;
    private CustomerInfo customer;
}
