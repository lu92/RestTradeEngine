package com.tradeengine.ProfileReader;

import com.tradeengine.ProfileReader.services.ProfileReaderServiceImpl;
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
@EnableAutoConfiguration(exclude = { ProfileReaderServiceImpl.class })
@ComponentScan(basePackages = { "com.tradeengine.ProfileReader.entities", "com.tradeengine.ProfileReader.repositories",
        "com.tradeengine.ProfileReader.controllers", "com.tradeengine.ProfileReader.mapper" })
public class TestContext extends WebMvcConfigurerAdapter
{

    @Bean
    public ProfileReaderServiceImpl profileReaderService()
    {
        return Mockito.mock(ProfileReaderServiceImpl.class);
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateConverter("yyyy-MM-dd"));
        registry.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        registry.addConverter(new CustomerConverter());
    }

}
