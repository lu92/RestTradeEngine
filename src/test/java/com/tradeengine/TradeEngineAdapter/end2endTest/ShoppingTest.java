package com.tradeengine.TradeEngineAdapter.end2endTest;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.TestUtils;
import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.adapter.TradeEngineGateway;
import com.tradeengine.TradeEngineAdapter.services.downlines.*;
import com.tradeengine.common.Message;
import com.tradeengine.common.entities.Price;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import static com.tradeengine.ProfileReaderTestData.*;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_CATEGORY_NAME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_PRODUCT_SCHEME;
import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.NUMBER;
import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.TEXT;
import static com.tradeengine.common.Message.Status.SUCCESS;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShoppingTest {

    Logger logger = Logger.getLogger(this.getClass().getName());


    // Application must be started

    private ProfileReaderRestService profileReaderRestService = new ProfileReaderRestService();
    private TradeEngineRestService tradeEngineRestService = new TradeEngineRestService();
    private ShoppingHistoryRestService shoppingHistoryRestService = new ShoppingHistoryRestService();
    private DynamicRetailerRestService dynamicRetailerRestService = new DynamicRetailerRestService();
    private BasketRestService basketRestService =
            new BasketRestService(profileReaderRestService, tradeEngineRestService, shoppingHistoryRestService, dynamicRetailerRestService);

    private TradeEngineGateway tradeEngineGateway =
            new TradeEngineGateway(profileReaderRestService, tradeEngineRestService, shoppingHistoryRestService, basketRestService);


    private static CustomerDto customerDto;
    private static CategoryDto categoryDto;
    private static ProductDto sgs7productDto;
    private static ProductDto sgs6productDto;
    private static ProductDto sgs4productDto;
    private static ProductDto iphone6SproductDto;

    @Test
    public void _01_createCustomer() throws IOException {
        final CreateCustomerDto CREATE_CUSTOMER_DTO
                = new CreateCustomerDto(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, EMAIL,
                CUSTOMER_BIRTHDAY, ADDRESS, CREDIT_CARD, USERNAME, PASSWORD);

        logger.info(TestUtils.convertObjectToJsonText(CREATE_CUSTOMER_DTO));
        customerDto = tradeEngineGateway.createCustomerAndHisShoppingHistory(CREATE_CUSTOMER_DTO);
        logger.info(TestUtils.convertObjectToJsonText(customerDto));

        assertThat(customerDto.getMessage()).isEqualTo(new Message("customer has been added!", Message.Status.SUCCESS));
    }

    @Test
    public void _02_createCategory() throws IOException {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto(PHONES_CATEGORY_NAME, null, PHONES_PRODUCT_SCHEME);
        logger.info(TestUtils.convertObjectToJsonText(createCategoryDto));

        categoryDto = tradeEngineGateway.createCategory(createCategoryDto);
        logger.info(TestUtils.convertObjectToJsonText(categoryDto));
    }


    @Test
    public void _03_createProducts() throws IOException {
        // SGS 6
        final CreateProductDto createSGS6ProductDto = new CreateProductDto(categoryDto.getCategoryInfo().getCategoryId(),
                "SAMSUNG GALAXY S6", "SAMSUNG GALAXY S6 Description", 10,
                new Price(1000, 250, 1250, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType(NUMBER).build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build()
        ));

        logger.info(TestUtils.convertObjectToJsonText(createSGS6ProductDto));
        sgs6productDto = tradeEngineGateway.addProduct(createSGS6ProductDto);
        logger.info(TestUtils.convertObjectToJsonText(sgs6productDto));

        assertThat(sgs6productDto.getMessage()).isEqualTo(new Message("Product has been added!", SUCCESS));

        // SGS 7
        final CreateProductDto createSGS7ProductDto = new CreateProductDto(categoryDto.getCategoryInfo().getCategoryId(),
                "SAMSUNG GALAXY S7", "SAMSUNG GALAXY S7 Description", 10,
                new Price(2000, 500, 2500, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType(NUMBER).build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build()
        ));

        logger.info(TestUtils.convertObjectToJsonText(createSGS7ProductDto));
        sgs7productDto = tradeEngineGateway.addProduct(createSGS7ProductDto);
        logger.info(TestUtils.convertObjectToJsonText(sgs7productDto));

        assertThat(sgs7productDto.getMessage()).isEqualTo(new Message("Product has been added!", SUCCESS));


        // SGS 4
        final CreateProductDto createSGS4ProductDto = new CreateProductDto(categoryDto.getCategoryInfo().getCategoryId(),
                "SAMSUNG GALAXY S4", "SAMSUNG GALAXY S4 Description", 15,
                new Price(500, 125, 625, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("5.0").unitOfValue("cal").valueType(NUMBER).build(),
                ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build()
        ));

        logger.info(TestUtils.convertObjectToJsonText(createSGS4ProductDto));
        sgs4productDto = tradeEngineGateway.addProduct(createSGS4ProductDto);
        logger.info(TestUtils.convertObjectToJsonText(sgs4productDto));

        assertThat(sgs4productDto.getMessage()).isEqualTo(new Message("Product has been added!", SUCCESS));

        // IPHONE6S
        final CreateProductDto createIphone6sProductDto = new CreateProductDto(categoryDto.getCategoryInfo().getCategoryId(),
                "IPHONE 6S", "IPHONE 6S Description", 15,
                new Price(2200, 600, 2800, "PLN"), "image path", Arrays.asList(
                ProductSpecification.builder().property("screen").value("4.7").unitOfValue("cal").valueType(NUMBER).build(),
                ProductSpecification.builder().property("producer").value("Apple").unitOfValue("").valueType(TEXT).build()
        ));

        logger.info(TestUtils.convertObjectToJsonText(createIphone6sProductDto));
        iphone6SproductDto = tradeEngineGateway.addProduct(createIphone6sProductDto);
        logger.info(TestUtils.convertObjectToJsonText(iphone6SproductDto));

        assertThat(iphone6SproductDto.getMessage()).isEqualTo(new Message("Product has been added!", SUCCESS));
    }

    @Test
    public void _04_makeProductsAvailable() throws IOException {
        tradeEngineGateway.activateProduct(sgs6productDto.getProductInfo().getProductId());
        tradeEngineGateway.activateProduct(sgs7productDto.getProductInfo().getProductId());
        tradeEngineGateway.activateProduct(sgs4productDto.getProductInfo().getProductId());
        tradeEngineGateway.activateProduct(iphone6SproductDto.getProductInfo().getProductId());
    }

    @Test
    public void _05_checkProductsAvailablility() throws IOException {
        ProductListDto productListDto = tradeEngineGateway.getProductList(
                new RequestedProductsDto(Arrays.asList(
                        sgs7productDto.getProductInfo().getProductId(),
                        sgs6productDto.getProductInfo().getProductId(),
                        sgs4productDto.getProductInfo().getProductId(),
                        iphone6SproductDto.getProductInfo().getProductId())));

        logger.info(TestUtils.convertObjectToJsonText(productListDto));

        assertThat(productListDto.getProduct(sgs7productDto.getProductInfo().getProductId()).isPresent()).isTrue();
        assertThat(productListDto.getProduct(sgs7productDto.getProductInfo().getProductId()).get().isAvailable()).isTrue();

        assertThat(productListDto.getProduct(sgs6productDto.getProductInfo().getProductId()).isPresent()).isTrue();
        assertThat(productListDto.getProduct(sgs6productDto.getProductInfo().getProductId()).get().isAvailable()).isTrue();

        assertThat(productListDto.getProduct(sgs4productDto.getProductInfo().getProductId()).isPresent()).isTrue();
        assertThat(productListDto.getProduct(sgs4productDto.getProductInfo().getProductId()).get().isAvailable()).isTrue();

        assertThat(productListDto.getProduct(iphone6SproductDto.getProductInfo().getProductId()).isPresent()).isTrue();
        assertThat(productListDto.getProduct(iphone6SproductDto.getProductInfo().getProductId()).get().isAvailable()).isTrue();
    }

    @Test
    public void _06_loginCustomer() throws IOException {
        CustomerDto login = tradeEngineGateway.login(
                customerDto.getCustomer().getUsername(),
                customerDto.getCustomer().getPassword());

        assertThat(login.getMessage()).isEqualTo(new Message("ACCESS GRANTED!", Message.Status.SUCCESS));
    }

    @Test
    public void _07_basketQuery() throws IOException {

        sgs7productDto.getProductInfo().setQuantity(5);

        // creating first basket query
        Basket basket = new Basket(
                customerDto.getCustomer().getCustomerId(),
                Arrays.asList(sgs6productDto.getProductInfo(), sgs7productDto.getProductInfo()),
                customerDto.getCustomer().getAddress());

        logger.info(TestUtils.convertObjectToJsonText(basket));

        // invoke shopping process
        Order order = tradeEngineGateway.doShopping(basket);
        logger.info(TestUtils.convertObjectToJsonText(order));

        ShoppingHistoryDto shoppingHistory = shoppingHistoryRestService.getShoppingHistory(customerDto.getCustomer().getCustomerId());
        logger.info(TestUtils.convertObjectToJsonText(shoppingHistory));

        CustomerDTO customerWithShoppingHistory = tradeEngineGateway.getCustomerWithShoppingHistory(customerDto.getCustomer().getCustomerId());
        logger.info(TestUtils.convertObjectToJsonText(customerWithShoppingHistory));

        assertThat(customerWithShoppingHistory.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(customerWithShoppingHistory.getCustomer().getShoppingHistory().getOrderList()).isNotEmpty();

        Order savedOrder = customerWithShoppingHistory.getCustomer().getShoppingHistory().getOrderList().get(0);
        assertThat(savedOrder.getProductList()).hasSize(2);
        assertThat(savedOrder.getProductList().get(0).getCommercialName()).isEqualTo(sgs6productDto.getProductInfo().getCommercialName());
        assertThat(savedOrder.getProductList().get(1).getCommercialName()).isEqualTo(sgs7productDto.getProductInfo().getCommercialName());


        // validate first order price
        assertThat(savedOrder.getPrice()).isEqualTo(
                Price.builder().amount(19800).tax(4950).price(24750).currency("PLN").build());

        // validate shopping-history's price
        assertThat(customerWithShoppingHistory.getCustomer().getShoppingHistory().getTotalPrice()).isEqualTo(
                Price.builder().amount(19800).tax(4950).price(24750).currency("PLN").build());
    }

    @Test
    public void _08_validateCustomerBalance() throws IOException {
        double balance = customerDto.getCustomer().getCreditCard().getBalance();
        CustomerDto customer = tradeEngineGateway.getCustomer(customerDto.getCustomer().getCustomerId());
        assertThat(customer.getCustomer().getCreditCard().getBalance()).isEqualTo(balance - 24750);
    }

    @Test
    public void _09_checkProductAvability() throws IOException {
        ProductListDto productListDto = tradeEngineGateway.getProductList(
                new RequestedProductsDto(Arrays.asList(
                        sgs7productDto.getProductInfo().getProductId(),
                        sgs6productDto.getProductInfo().getProductId())));

        logger.info(TestUtils.convertObjectToJsonText(productListDto));

        // sgs7's quantity should equal to 5
        assertThat(productListDto.getProduct(sgs7productDto.getProductInfo().getProductId()).isPresent()).isTrue();
        assertThat(productListDto.getProduct(sgs7productDto.getProductInfo().getProductId()).get().isAvailable()).isTrue();
        assertThat(productListDto.getProduct(sgs7productDto.getProductInfo().getProductId()).get().getQuantity()).isEqualTo(5);


        // sgs6 should not be available
        assertThat(productListDto.getProduct(sgs6productDto.getProductInfo().getProductId()).isPresent()).isTrue();
        assertThat(productListDto.getProduct(sgs6productDto.getProductInfo().getProductId()).get().isAvailable()).isFalse();
        assertThat(productListDto.getProduct(sgs6productDto.getProductInfo().getProductId()).get().getQuantity()).isEqualTo(0);
    }

    @Test
    public void _10_basketQuery() throws IOException {

        sgs4productDto.getProductInfo().setQuantity(10);
        iphone6SproductDto.getProductInfo().setQuantity(10);

        // creating second basket query
        Basket basket = new Basket(
                customerDto.getCustomer().getCustomerId(),
                Arrays.asList(sgs4productDto.getProductInfo(), iphone6SproductDto.getProductInfo()),
                customerDto.getCustomer().getAddress());

        logger.info(TestUtils.convertObjectToJsonText(basket));

        // invoke shopping process
        Order order = tradeEngineGateway.doShopping(basket);
        logger.info(TestUtils.convertObjectToJsonText(order));

        ShoppingHistoryDto shoppingHistory = shoppingHistoryRestService.getShoppingHistory(customerDto.getCustomer().getCustomerId());
        logger.info(TestUtils.convertObjectToJsonText(shoppingHistory));

        CustomerDTO customerWithShoppingHistory = tradeEngineGateway.getCustomerWithShoppingHistory(customerDto.getCustomer().getCustomerId());
        logger.info(TestUtils.convertObjectToJsonText(customerWithShoppingHistory));

        assertThat(customerWithShoppingHistory.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(customerWithShoppingHistory.getCustomer().getShoppingHistory().getOrderList()).isNotEmpty();

        Order savedOrder = customerWithShoppingHistory.getCustomer().getShoppingHistory().getOrderList().get(1);
        assertThat(savedOrder.getProductList()).hasSize(2);
        assertThat(savedOrder.getProductList().get(0).getCommercialName()).isEqualTo(sgs4productDto.getProductInfo().getCommercialName());
        assertThat(savedOrder.getProductList().get(1).getCommercialName()).isEqualTo(iphone6SproductDto.getProductInfo().getCommercialName());


        // validate second order price
        assertThat(savedOrder.getPrice()).isEqualTo(
                Price.builder().amount(26730.0).tax(7177.5).price(33907.5).currency("PLN").build());

        // validate shopping-history's price
        assertThat(customerWithShoppingHistory.getCustomer().getShoppingHistory().getTotalPrice()).isEqualTo(
                Price.builder().amount(46530.0).tax(12127.5).price(58657.5).currency("PLN").build());
    }

    @Test
    public void _11_validateCustomerBalance() throws IOException {
        double balance = customerDto.getCustomer().getCreditCard().getBalance();
        CustomerDto customer = tradeEngineGateway.getCustomer(customerDto.getCustomer().getCustomerId());
        assertThat(customer.getCustomer().getCreditCard().getBalance()).isEqualTo(balance - 58657.5);
    }




}
