package com.tradeengine.TradeEngine.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeengine.TradeEngine.dto.productCriteria.ValueType;
import org.junit.Test;

import static java.util.Arrays.asList;

public class ProductSchemeTest {

    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ProductScheme productSchemeDto = new ProductScheme();
        productSchemeDto.setCategoryName("Phones");
        productSchemeDto.setProductSchemeElements(asList(
                new ProductSchemeElement("producer", ValueType.TEXT, ""),
                new ProductSchemeElement("screen", ValueType.NUMBER, "cal")));

//        productSchemeDto.setBasicProductSchema(new HashMap<String, String>(){{
//            put("producer", "java.lang.String.class");
//            put("screen", "java.lang.Integer.class");
//        }});

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(productSchemeDto);

        // pretty print
        System.out.println(json);
    }

}
