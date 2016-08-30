package com.tradeengine.ProfileReader.mapper;

import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.ProfileReader.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class ProfileReaderMapper {
    public static CustomerInfo mapCustomer(Customer customer) {
        return new CustomerInfo(
                customer.getCustomerId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getBirthday(),
                customer.getAddress(),
                customer.getCreditCard(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getCreationDate(),
                customer.getPoints(),
                customer.getTierLevel()
        );
    }

    public static Customer mapCustomer(CustomerInfo customerInfo) {
        return new Customer(
                customerInfo.getCustomerId(),
                customerInfo.getFirstname(),
                customerInfo.getLastname(),
                customerInfo.getEmail(),
                customerInfo.getBirthday(),
                customerInfo.getAddress(),
                customerInfo.getCreditCard(),
                customerInfo.getUsername(),
                customerInfo.getPassword(),
                customerInfo.getCreationDate(),
                customerInfo.getPoints(),
                customerInfo.getTierLevel()
        );
    }
}
