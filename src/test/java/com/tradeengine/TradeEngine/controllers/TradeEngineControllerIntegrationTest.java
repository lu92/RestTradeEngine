package com.tradeengine.TradeEngine.controllers;

import com.tradeengine.ProfileReader.services.ProfileReaderService;
import com.tradeengine.TestUtils;
import com.tradeengine.TradeEngine.TradeEngineServiceContext;
import com.tradeengine.common.Message;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_CATEGORY;
import static com.tradeengine.TradeEngine.TradeEngineTestData.CATEGORY_22;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_CATEGORY_NAME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.LAPTOPS_CATEGORY_NAME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_COMPUTER;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TradeEngineServiceContext.class })
@WebAppConfiguration
//@IntegrationTest
//@Rollback(true)
public class TradeEngineControllerIntegrationTest
{

    @Autowired
    private WebApplicationContext webCtx;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webCtx).build();
    }

    public static final String TRADE_ENGINE_CATEGORY_URL = "/TradeEngine/Category";
    public static final String TRADE_ENGINE_PRODUCT_URL = "/TradeEngine/Product";

    @Test
    @Transactional
    public void testGetCategory() throws Exception
    {
        PHONES_CATEGORY.setCategoryId(1L);

        mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.categoryId", is(1)))
                .andExpect(jsonPath("$.category.name", is(PHONES_CATEGORY_NAME)))
                .andExpect(jsonPath("$.productList", Matchers.empty()));
    }

//    @Test
//    @Transactional
//    public void testGetCategoryList() throws Exception
//    {
//        PHONES_CATEGORY.setCategoryId(1L);
//        CATEGORY_22.setCategoryId(2L);
//
////        Mockito.when(tra)
//
//        mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0]", is(PHONES_CATEGORY_NAME)))
//                .andExpect(jsonPath("$[1]", is(LAPTOPS_CATEGORY_NAME)));
//    }
//
//    @Test
//    @Transactional
//    public void testDeleteCategory() throws Exception
//    {
//        Message message = new Message("message from TradeEngineService2Impl", Message.Status.SUCCESS);
//
//        mockMvc.perform(delete(TRADE_ENGINE_CATEGORY_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message", is(message.getMessage())))
//                .andExpect(jsonPath("$.status", is(Message.Status.SUCCESS.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void testGetProduct() throws Exception
//    {
//        PRODUCT.setProductId(1L);
//        PRODUCT.setCategory(PHONES_CATEGORY);
//
//        mockMvc.perform(get(TRADE_ENGINE_PRODUCT_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.productId", is(1)))
//                .andExpect(jsonPath("$.commercialName", is(PRODUCT.getCommercialName())))
//                .andExpect(jsonPath("$.price.amount", is(PRODUCT.getPrice().getAmount())))
//                .andExpect(jsonPath("$.price.tax", is(PRODUCT.getPrice().getTax())))
//                .andExpect(jsonPath("$.price.price", is(PRODUCT.getPrice().getPrice())))
//                .andExpect(jsonPath("$.category.name", is(PRODUCT.getCategory().getName())));
//    }
//
//    @Test
//    @Transactional
//    public void testGetProductList() throws Exception
//    {
//        PRODUCT.setProductId(1L);
//        PRODUCT.setCategory(PHONES_CATEGORY);
//        PRODUCT_COMPUTER.setProductId(2L);
//        PRODUCT_COMPUTER.setCategory(CATEGORY_22);
//
//        mockMvc.perform(get(TRADE_ENGINE_PRODUCT_URL))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].productId", is(PRODUCT.getProductId().intValue())))
//                .andExpect(jsonPath("$[0].commercialName", is(PRODUCT.getCommercialName())))
//                .andExpect(jsonPath("$[0].price.amount", is(PRODUCT.getPrice().getAmount())))
//                .andExpect(jsonPath("$[0].price.tax", is(PRODUCT.getPrice().getTax())))
//                .andExpect(jsonPath("$[0].price.price", is(PRODUCT.getPrice().getPrice())))
//                .andExpect(jsonPath("$[0].category.name", is(PRODUCT.getCategory().getName())))
//                .andExpect(jsonPath("$[0].productSpecificationList[0].property", is(PRODUCT.getProductSpecificationList().get(0).getProperty())))
//                .andExpect(jsonPath("$[0].productSpecificationList[0].value", is(PRODUCT.getProductSpecificationList().get(0).getValue())))
//                .andExpect(jsonPath("$[0].productSpecificationList[0].unitOfValue", is(PRODUCT.getProductSpecificationList().get(0).getUnitOfValue())))
//                .andExpect(jsonPath("$[0].productSpecificationList[0].valueType", is(PRODUCT.getProductSpecificationList().get(0).getValueType())))
//                .andExpect(jsonPath("$[1].productId", is(PRODUCT_COMPUTER.getProductId().intValue())))
//                .andExpect(jsonPath("$[1].commercialName", is(PRODUCT_COMPUTER.getCommercialName())))
//                .andExpect(jsonPath("$[1].price.amount", is(PRODUCT_COMPUTER.getPrice().getAmount())))
//                .andExpect(jsonPath("$[1].price.tax", is(PRODUCT_COMPUTER.getPrice().getTax())))
//                .andExpect(jsonPath("$[1].price.price", is(PRODUCT_COMPUTER.getPrice().getPrice())))
//                .andExpect(jsonPath("$[1].category.name", is(PRODUCT_COMPUTER.getCategory().getName())))
//                .andExpect(jsonPath("$[1].productSpecificationList[0].property", is(PRODUCT_COMPUTER.getProductSpecificationList().get(0).getProperty())))
//                .andExpect(jsonPath("$[1].productSpecificationList[0].value", is(PRODUCT_COMPUTER.getProductSpecificationList().get(0).getValue())))
//                .andExpect(jsonPath("$[1].productSpecificationList[0].unitOfValue", is(PRODUCT_COMPUTER.getProductSpecificationList().get(0).getUnitOfValue())))
//                .andExpect(jsonPath("$[1].productSpecificationList[0].valueType", is(PRODUCT_COMPUTER.getProductSpecificationList().get(0).getValueType())));
//    }
//
//    @Ignore
//    public void testAddProduct() throws Exception
//    {
//        Message message = new Message("", Message.Status.SUCCESS);
//        System.out.println(TestUtils.convertObjectToJsonText(PRODUCT));
//        mockMvc.perform(post(TRADE_ENGINE_PRODUCT_URL + "/1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(TestUtils.convertObjectToJsonText(PRODUCT)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
//    }
//
//    @Test
//    @Transactional
//    public void testDeleteProduct() throws Exception
//    {
//        Message message = new Message("message from TradeEngineService2Impl", Message.Status.SUCCESS);
//
//        mockMvc.perform(delete(TRADE_ENGINE_PRODUCT_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message", is(message.getMessage())))
//                .andExpect(jsonPath("$.status", is(Message.Status.SUCCESS.toString())));
//    }
}
