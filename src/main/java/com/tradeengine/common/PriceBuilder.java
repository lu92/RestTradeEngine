package com.tradeengine.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeengine.common.entities.Price;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PriceBuilder {
    Price price;
    ObjectMapper mapper = new ObjectMapper();


    PriceBuilder(double amount,
                 double tax,
                 double price,
                 String currency) {
        try {
            Currencies currencies = mapper.readValue(new File("/price-conf.json"), Currencies.class);
            currencies.getCurrencyList().stream().filter(currenctCurrency -> currenctCurrency.getCurrency().equals(currency)).findFirst();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
