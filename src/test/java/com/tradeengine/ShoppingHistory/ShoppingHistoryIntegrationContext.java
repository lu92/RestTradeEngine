package com.tradeengine.ShoppingHistory;

import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.tradeengine.ShoppingHistory.entities", "com.tradeengine.ShoppingHistory.repositories",
        "com.tradeengine.ShoppingHistory.services", "com.tradeengine.ShoppingHistory.mapper" })
public class ShoppingHistoryIntegrationContext
{
    @Bean
    public CustomerRepository customerRepository()
    {
        return Mockito.mock(CustomerRepository.class);
    }
}
