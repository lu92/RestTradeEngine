package com.tradeengine.ProfileReader;

import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDtoList
{
    private Message message;
    private List<Customer> customerList;
}
