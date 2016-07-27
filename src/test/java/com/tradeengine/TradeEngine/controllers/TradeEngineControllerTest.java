package com.tradeengine.TradeEngine.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeengine.TestUtils;
import com.tradeengine.TradeEngine.TradeEngineTestContext;
import com.tradeengine.TradeEngine.dto.CategoryDto;
import com.tradeengine.TradeEngine.dto.CategoryListDto;
import com.tradeengine.TradeEngine.dto.CreateCategoryDto;
import com.tradeengine.TradeEngine.dto.CreateProductDto;
import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngine.dto.ProductScheme;
import com.tradeengine.TradeEngine.dto.ProductSchemeDto;
import com.tradeengine.TradeEngine.dto.ProductSpecification;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.mappers.TradeEngineMapper;
import com.tradeengine.TradeEngine.services.TradeEngineServiceImpl;
import com.tradeengine.common.Message;
import com.tradeengine.common.entities.Price;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tradeengine.ShoppingHistoryTestData.COMPLETED_ORDER_1;
import static com.tradeengine.TradeEngine.TradeEngineTestData.FAKE_CATEGORY_ID;
import static com.tradeengine.TradeEngine.TradeEngineTestData.LAPTOPS_CATEGORY_NAME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_CATEGORY;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_CATEGORY_NAME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_SGS7;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_PRODUCT_SCHEME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PROPER_CATEGORY_ID;
import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TradeEngineTestContext.class })
@WebAppConfiguration
public class TradeEngineControllerTest
{
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private WebApplicationContext webCtx;

    @Autowired
    private TradeEngineServiceImpl tradeEngineServiceMock;

    @Autowired
    private TradeEngineMapper tradeEngineMapper;

    private MockMvc mockMvc;

    public static final String TRADE_ENGINE_CATEGORY_URL = "/TradeEngine/Category";
    public static final String TRADE_ENGINE_PRODUCT_URL = "/TradeEngine/Product";

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webCtx).build();
    }

    @Test
    public void testGetCategoryWhenDoesNotExist() throws Exception
    {
        CategoryDto categoryDto = new CategoryDto(new Message("Selected category doesn't exist!", FAILURE), null);
        when(tradeEngineServiceMock.getCategory(anyLong())).thenReturn(categoryDto);

        final long MISSING_CATEGORY_ID = -1;

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL + "/" + FAKE_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Selected category doesn't exist!")))
                .andExpect(jsonPath("$.message.status", equalTo(FAILURE.toString())))
                .andExpect(jsonPath("$.category", isEmptyOrNullString())).andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCategoryWhenExist() throws Exception
    {
        CategoryDto categoryDto = new CategoryDto(new Message("Category has been delivered!", SUCCESS), PHONES_CATEGORY);
        when(tradeEngineServiceMock.getCategory(anyLong())).thenReturn(categoryDto);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL + "/" + PROPER_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Category has been delivered!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.category.name", equalTo(PHONES_CATEGORY.getName())))
                .andExpect(jsonPath("$.category.name", equalTo(PHONES_CATEGORY.getName()))).andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCategoryListWhenEmpty() throws Exception
    {
        CategoryListDto categoryListDto = new CategoryListDto(new Message("category list is empty", SUCCESS), true, new ArrayList<>());
        when(tradeEngineServiceMock.getCategoryList()).thenReturn(categoryListDto);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("category list is empty")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.empty", equalTo(true)))
                .andExpect(jsonPath("$.categoryList", empty())).andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCategoryListWhenContainsTwoCategories() throws Exception
    {
        CategoryListDto categoryListDto = new CategoryListDto(new Message("category list is filled", SUCCESS), false, asList(PHONES_CATEGORY_NAME, LAPTOPS_CATEGORY_NAME));
        when(tradeEngineServiceMock.getCategoryList()).thenReturn(categoryListDto);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("category list is filled")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.empty", equalTo(false)))
                .andExpect(jsonPath("$.categoryList[0]", equalTo(categoryListDto.getCategoryList().get(0))))
                .andExpect(jsonPath("$.categoryList[1]", equalTo(categoryListDto.getCategoryList().get(1))))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetProductSchemeForCategory() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ProductScheme phoneProductScheme = mapper.readValue(PHONES_PRODUCT_SCHEME, ProductScheme.class);
        ProductSchemeDto productSchemeDto = new ProductSchemeDto(new Message("Product scheme has been delivered!", SUCCESS), phoneProductScheme);

        when(tradeEngineServiceMock.getProductSchemeForCategory(anyLong())).thenReturn(productSchemeDto);

        String RQ = TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1);
        logger.info("RQ = " + RQ);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL + "/GetProductScheme/" + PROPER_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Product scheme has been delivered!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.productScheme.categoryName", equalTo(productSchemeDto.getProductScheme().getCategoryName())))
                .andExpect(jsonPath("$.productScheme.basicProductSchema", equalTo(productSchemeDto.getProductScheme().getBasicProductSchema())))

                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testCreateCategoryWithoutParent() throws Exception
    {
        final CreateCategoryDto createCategoryDto = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);

        String RQ = TestUtils.convertObjectToJsonText(createCategoryDto);
        logger.info("RQ = " + RQ);

        CategoryDto categoryDto = new CategoryDto(new Message("Category has been created!", SUCCESS), PHONES_CATEGORY);

        when(tradeEngineServiceMock.createCategory(createCategoryDto)).thenReturn(categoryDto);

        String RS = mockMvc.perform(post(TRADE_ENGINE_CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Category has been created!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.category.name", equalTo(PHONES_CATEGORY.getName())))
                .andExpect(jsonPath("$.category.name", equalTo(PHONES_CATEGORY.getName())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddProductExpectSuccess() throws Exception
    {
        final CreateProductDto createProductDto = new CreateProductDto(1,"SAMSUNG GALAXY S7", "SAMSUNG GALAXY S7 Description", 10,
                new Price(2000, 500, 2500, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType("java.lang.Double.class").build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType("java.lang.String.class").build()
        ));

        String RQ = TestUtils.convertObjectToJsonText(createProductDto);
        logger.info("RQ = " + RQ);

        Product product = tradeEngineMapper.convertProduct(createProductDto);
        ProductDto productDto = new ProductDto(new Message("Product has been added!", SUCCESS), product);
        when(tradeEngineServiceMock.addProduct(createProductDto.getCategoryId(), product)).thenReturn(productDto);

        String RS = mockMvc.perform(post(TRADE_ENGINE_PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createProductDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Product has been added!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.product.commercialName", equalTo("SAMSUNG GALAXY S7")))
                .andExpect(jsonPath("$.product.productDescription", equalTo("SAMSUNG GALAXY S7 Description")))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddProductWhenCategoryDoesNotExistExpectFailure() throws Exception
    {
        final CreateProductDto createProductDto = new CreateProductDto(1,"SAMSUNG GALAXY S7", "SAMSUNG GALAXY S7 Description", 10,
                new Price(2000, 500, 2500, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType("java.lang.Double.class").build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType("java.lang.String.class").build()
        ));

        String RQ = TestUtils.convertObjectToJsonText(createProductDto);
        logger.info("RQ = " + RQ);

        Product product = tradeEngineMapper.convertProduct(createProductDto);
        ProductDto productDto = new ProductDto(new Message("Category doesn't exist!", FAILURE), null);
        when(tradeEngineServiceMock.addProduct(createProductDto.getCategoryId(), product)).thenReturn(productDto);

        System.out.println();
        String RS = mockMvc.perform(post(TRADE_ENGINE_PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createProductDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Category doesn't exist!")))
                .andExpect(jsonPath("$.message.status", equalTo(FAILURE.toString())))
                .andExpect(jsonPath("$.product", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }
}
