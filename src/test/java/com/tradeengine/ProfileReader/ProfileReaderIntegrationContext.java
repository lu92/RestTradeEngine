package com.tradeengine.ProfileReader;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.tradeengine.ProfileReader.entities", "com.tradeengine.ProfileReader.repositories", "com.tradeengine.ProfileReader.services" })
public class ProfileReaderIntegrationContext
{
}
