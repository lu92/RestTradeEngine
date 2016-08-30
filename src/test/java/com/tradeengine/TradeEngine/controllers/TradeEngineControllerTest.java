package com.tradeengine.TradeEngine.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeengine.TestUtils;
import com.tradeengine.TradeEngine.TradeEngineTestContext;
import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.dto.productCriteria.Criteria;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
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
import java.util.List;

import static com.tradeengine.ShoppingHistoryTestData.COMPLETED_ORDER_1;
import static com.tradeengine.TradeEngine.TradeEngineTestData.*;
import static com.tradeengine.TradeEngine.dto.productCriteria.Direct.*;
import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.NUMBER;
import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.TEXT;
import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TradeEngineTestContext.class})
@WebAppConfiguration
public class TradeEngineControllerTest {
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
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webCtx).build();
    }

    @Test
    public void testGetCategoryWhenDoesNotExist() throws Exception {
        CategoryDto categoryDto = new CategoryDto(new Message("Selected category doesn't exist!", FAILURE), null);
        when(tradeEngineServiceMock.getCategory(anyLong())).thenReturn(categoryDto);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL + "/" + FAKE_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Selected category doesn't exist!")))
                .andExpect(jsonPath("$.message.status", equalTo(FAILURE.toString())))
                .andExpect(jsonPath("$.categoryInfo", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCategoryWhichIsRootCategoryWithoutSubCategoriesExpectSuccess() throws Exception {
        CategoryDto categoryDto = new CategoryDto(new Message("Category has been delivered!", SUCCESS), PHONES_CATEGORY_INFO);
        when(tradeEngineServiceMock.getCategory(anyLong())).thenReturn(categoryDto);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL + "/" + PROPER_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Category has been delivered!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.categoryInfo.parentCategory", isEmptyOrNullString()))
                .andExpect(jsonPath("$.categoryInfo.name", equalTo(PHONES_CATEGORY.getName())))
                .andExpect(jsonPath("$.categoryInfo.subCategories", empty()))
                .andExpect(jsonPath("$.categoryInfo.productSchemaJsonFigure", equalTo(PHONES_CATEGORY.getProductSchemaJsonFigure())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCategoryListWhenEmpty() throws Exception {
        CategoryListDto categoryListDto = new CategoryListDto(new Message("category list is empty", SUCCESS), new ArrayList<>());
        when(tradeEngineServiceMock.getCategoryList()).thenReturn(categoryListDto);

        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("category list is empty")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.categoryList", empty())).andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCategoryListWhenContainsTwoCategories() throws Exception {
//        CategoryListDto categoryListDto = new CategoryListDto(new Message("category list is filled", SUCCESS), false, asList(PHONES_CATEGORY_NAME, LAPTOPS_CATEGORY_NAME));
//        when(tradeEngineServiceMock.getCategoryList()).thenReturn(categoryListDto);
//
//        String RS = mockMvc.perform(get(TRADE_ENGINE_CATEGORY_URL))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message.message", equalTo("category list is filled")))
//                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
//                .andExpect(jsonPath("$.empty", equalTo(false)))
//                .andExpect(jsonPath("$.categoryList[0]", equalTo(categoryListDto.getCategoryList().get(0))))
//                .andExpect(jsonPath("$.categoryList[1]", equalTo(categoryListDto.getCategoryList().get(1))))
//                .andReturn().getResponse().getContentAsString();
//
//        logger.info("RS = " + RS);
    }

    @Test
    public void testGetProductSchemeForCategory() throws Exception {
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
                .andExpect(jsonPath("$.productScheme.productSchemeElements[0].property", equalTo(productSchemeDto.getProductScheme().getProductSchemeElements().get(0).getProperty())))
                .andExpect(jsonPath("$.productScheme.productSchemeElements[0].valueType", equalTo(productSchemeDto.getProductScheme().getProductSchemeElements().get(0).getValueType().toString())))
                .andExpect(jsonPath("$.productScheme.productSchemeElements[0].unit", equalTo(productSchemeDto.getProductScheme().getProductSchemeElements().get(0).getUnit())))
                .andExpect(jsonPath("$.productScheme.productSchemeElements[1].property", equalTo(productSchemeDto.getProductScheme().getProductSchemeElements().get(1).getProperty())))
                .andExpect(jsonPath("$.productScheme.productSchemeElements[1].valueType", equalTo(productSchemeDto.getProductScheme().getProductSchemeElements().get(1).getValueType().toString())))
                .andExpect(jsonPath("$.productScheme.productSchemeElements[1].unit", equalTo(productSchemeDto.getProductScheme().getProductSchemeElements().get(1).getUnit())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testCreateCategoryWithoutParent() throws Exception {
        final CreateCategoryDto createCategoryDto = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);

        String RQ = TestUtils.convertObjectToJsonText(createCategoryDto);
        logger.info("RQ = " + RQ);

        CategoryDto categoryDto = new CategoryDto(new Message("Category has been created!", SUCCESS), PHONES_CATEGORY_INFO);

        when(tradeEngineServiceMock.createCategory(createCategoryDto)).thenReturn(categoryDto);

        String RS = mockMvc.perform(post(TRADE_ENGINE_CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Category has been created!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.categoryInfo.name", equalTo(PHONES_CATEGORY.getName())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddProductExpectSuccess() throws Exception {
        final CreateProductDto createProductDto = new CreateProductDto(1, "SAMSUNG GALAXY S7", "SAMSUNG GALAXY S7 Description", 10,
                new Price(2000, 500, 2500, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType(NUMBER).build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build()
        ));

        String RQ = TestUtils.convertObjectToJsonText(createProductDto);
        logger.info("RQ = " + RQ);

        Product product = tradeEngineMapper.convertProduct(createProductDto);
        ProductInfo productInfo = tradeEngineMapper.convertProduct(product);
        ProductDto productDto = new ProductDto(new Message("Product has been added!", SUCCESS), productInfo);
        when(tradeEngineServiceMock.addProduct(createProductDto)).thenReturn(productDto);

        String RS = mockMvc.perform(post(TRADE_ENGINE_PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createProductDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Product has been added!")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andExpect(jsonPath("$.productInfo.commercialName", equalTo("SAMSUNG GALAXY S7")))
                .andExpect(jsonPath("$.productInfo.productDescription", equalTo("SAMSUNG GALAXY S7 Description")))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddProductWhenCategoryDoesNotExistExpectFailure() throws Exception {
        final CreateProductDto createProductDto = new CreateProductDto(1, "SAMSUNG GALAXY S7", "SAMSUNG GALAXY S7 Description", 10,
                new Price(2000, 500, 2500, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType(NUMBER).build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build()
        ));

        String RQ = TestUtils.convertObjectToJsonText(createProductDto);
        logger.info("RQ = " + RQ);

//        Product product = tradeEngineMapper.convertProduct(createProductDto);
        ProductDto productDto = new ProductDto(new Message("Category doesn't exist!", FAILURE), null);
        when(tradeEngineServiceMock.addProduct(createProductDto)).thenReturn(productDto);

        System.out.println();
        String RS = mockMvc.perform(post(TRADE_ENGINE_PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createProductDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Category doesn't exist!")))
                .andExpect(jsonPath("$.message.status", equalTo(FAILURE.toString())))
                .andExpect(jsonPath("$.productInfo", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testUpdateProductExpectSuccess() throws Exception {
        Product productForUpdate = PRODUCT_SGS7;

        String RQ = TestUtils.convertObjectToJsonText(productForUpdate);
        logger.info("RQ = " + RQ);
    }

    @Test
    public void testFindProductsByCriteria() throws Exception {
        ProductCriteria productCriteria = ProductCriteria.builder()
                .category(PHONES_CATEGORY_NAME)
                .minPrice(1000L)
                .maxPrice(3000L)
                .criteriaList(asList(
                        new Criteria("screen", "5", "cal", NUMBER, GREATER_THAN_OR_EQUAL),
                        new Criteria("screen", "6", "cal", NUMBER, LOWER),
                        new Criteria("producer", "Samsung", "", TEXT, EQUAL)))
                .build();

        String RQ = TestUtils.convertObjectToJsonText(productCriteria);
        logger.info("RQ = " + RQ);

        List<ProductInfo> productInfoList = tradeEngineMapper.convertProductList(asList(PRODUCT_SGS6, PRODUCT_SGS7));
        ProductListDto productListDto = new ProductListDto(new Message("List of founded products", SUCCESS), productCriteria.getCategory(), productInfoList);
        when(tradeEngineServiceMock.findProducts(productCriteria)).thenReturn(productListDto);

        System.out.println();
        String RS = mockMvc.perform(post(TRADE_ENGINE_PRODUCT_URL + "/FindByCriteria")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(productCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("List of founded products")))
                .andExpect(jsonPath("$.message.status", equalTo(SUCCESS.toString())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

}
