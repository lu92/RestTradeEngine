package com.tradeengine.TradeEngine.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;

public class ProductSchemeTest
{
    @Test
    public void test() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        ProductScheme productShemaDto = new ProductScheme();
        productShemaDto.setCategoryName("Phones");
        productShemaDto.setBasicProductSchema(new HashMap<String, String>(){{
            put("producent", "java.lang.String.class");
            put("screen", "java.lang.Integer.class");
        }});

        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(productShemaDto);

        // pretty print
        System.out.println(json);
    }

}
