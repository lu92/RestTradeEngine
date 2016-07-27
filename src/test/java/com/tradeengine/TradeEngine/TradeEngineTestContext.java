package com.tradeengine.TradeEngine;

import com.tradeengine.TradeEngine.services.TradeEngineServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = { TradeEngineServiceImpl.class })
@ComponentScan(basePackages = { "com.tradeengine.TradeEngine.entities", "com.tradeengine.TradeEngine.mappers", "com.tradeengine.TradeEngine.repositories", "com.tradeengine.TradeEngine.controllers" })
public class TradeEngineTestContext
{
    @Bean
    public TradeEngineServiceImpl tradeEngineService()
    {
        return Mockito.mock(TradeEngineServiceImpl.class);
    }
}
