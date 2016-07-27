package com.tradeengine.ShoppingHistory;

import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.ShoppingHistory.services.ShoppingHistoryServiceImpl;
import com.tradeengine.common.CustomerConverter;
import com.tradeengine.common.LocalDateConverter;
import com.tradeengine.common.LocalDateTimeConverter;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAutoConfiguration(exclude = { ShoppingHistoryServiceImpl.class })
@ComponentScan(basePackages = { "com.tradeengine.ShoppingHistory.entities", "com.tradeengine.ShoppingHistory.repositories", "com.tradeengine.ShoppingHistory.controllers" })
public class ShoppingHistoryTestContext extends WebMvcConfigurerAdapter
{

    @Bean
    public ShoppingHistoryServiceImpl shoppingHistoryService()
    {
        return Mockito.mock(ShoppingHistoryServiceImpl.class);
    }

    @Bean
    public CustomerRepository customerRepository() {return Mockito.mock(CustomerRepository.class); }

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addConverter(new LocalDateConverter("yyyy-MM-dd"));
        registry.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        registry.addConverter(new CustomerConverter());
    }

}
