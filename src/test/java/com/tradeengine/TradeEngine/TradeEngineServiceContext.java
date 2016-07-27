package com.tradeengine.TradeEngine;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.tradeengine.TradeEngine.entities", "com.tradeengine.TradeEngine.mappers", "com.tradeengine.TradeEngine.repositories", "com.tradeengine.TradeEngine.services", "com.tradeengine.TradeEngine.controllers" })
public class TradeEngineServiceContext extends WebMvcConfigurerAdapter
{
}
