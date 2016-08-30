package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngine.mappers.TradeEngineMapper;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Error;
import com.tradeengine.TradeEngineAdapter.model.ErrorType;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.common.Message;
import com.tradeengine.common.entities.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_IPHONE_6S;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_SGS7;
import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.PARTIAL_SUCCESS;
import static com.tradeengine.common.Message.Status.SUCCESS;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BasketRestServiceTest {

    @Mock
    private ShoppingHistoryRestService shoppingHistoryRestService;

    @Mock
    private TradeEngineRestService tradeEngineRestService;

    @InjectMocks
    private BasketRestService basketRestService;

    private TradeEngineMapper tradeEngineMapper = new TradeEngineMapper();

    private Long customerId = 1L;
    private Address address = new Address("Abbey Road 1", "London", "20-100 London", "England");

    @Test
    public void testEmptyProductListExpectFailure() {
        // given
        Basket basket = new Basket(customerId, new ArrayList<>(), address);

        // when
        Order order = basketRestService.doShopping(basket);

        //then
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getTimeOfSale()).isNotNull();
        assertThat(order.getProductList()).isEmpty();
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getPrice()).isEqualTo(new Price(0, 0, 0, "PLN"));
        assertThat(order.getFlowResults()).containsExactly(new Error(null, "", "Basket is Empty!", ErrorType.EMPTY_BASKET));
        assertThat(order.getGainedPoints()).isEqualTo(0L);
        assertThat(order.getDiscountList()).isEmpty();
        assertThat(order.getStatus()).isEqualTo(Message.Status.FAILURE);
    }

    @Test
    public void testAllProductsAreNotAvailableExpectFailure() {
        // given
        List<ProductInfo> products = products();
        ProductInfo _IPHONE_6S = products.get(0);
        ProductInfo _SGS7 = products.get(1);

        Basket basket = new Basket(customerId, products, address);
        ProductListDto productListDto = new ProductListDto(new Message("Any requested product does not exist!", FAILURE), null, new ArrayList<>());
        Mockito.when(tradeEngineRestService.getProductList(Mockito.any())).thenReturn(productListDto);

        // when
        Order order = basketRestService.doShopping(basket);

        //then
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getTimeOfSale()).isNotNull();
        assertThat(order.getProductList()).containsExactly(_IPHONE_6S, _SGS7);
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getPrice()).isEqualTo(new Price(0, 0, 0, "PLN"));
        assertThat(order.getFlowResults()).containsExactly(
                new Error(1L, _IPHONE_6S.getCommercialName(), "", ErrorType.PRODUCT_IS_NOT_AVAILABLE),
                new Error(2L, _SGS7.getCommercialName(), "", ErrorType.PRODUCT_IS_NOT_AVAILABLE));
        assertThat(order.getGainedPoints()).isEqualTo(0L);
        assertThat(order.getDiscountList()).isEmpty();
        assertThat(order.getStatus()).isEqualTo(Message.Status.FAILURE);
    }

    @Test
    public void testOneProductIsNotAvailableExpectFailure() {
        // given
        List<ProductInfo> products = products();
        ProductInfo _IPHONE_6S = products.get(0);
        ProductInfo _SGS7 = products.get(1);

        Basket basket = new Basket(customerId, products, address);
        ProductListDto productListDto = new ProductListDto(new Message("Some requested product has been delivered!", PARTIAL_SUCCESS), null, oneProductIsAvailable());
        Mockito.when(tradeEngineRestService.getProductList(Mockito.any())).thenReturn(productListDto);

        // when
        Order order = basketRestService.doShopping(basket);

        //then
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getTimeOfSale()).isNotNull();
        assertThat(order.getProductList()).containsExactly(_IPHONE_6S, _SGS7);
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getPrice()).isEqualTo(new Price(0, 0, 0, "PLN"));
        assertThat(order.getFlowResults()).containsExactly(
                new Error(2L, _SGS7.getCommercialName(), "", ErrorType.PRODUCT_IS_NOT_AVAILABLE));
        assertThat(order.getGainedPoints()).isEqualTo(0L);
        assertThat(order.getDiscountList()).isEmpty();
        assertThat(order.getStatus()).isEqualTo(Message.Status.FAILURE);
    }

    @Test
    public void testMissingCustomerIdAndAddressFirstProductIsLessSecondProductIsNotAvailableExpectFailure() {
        // given
        List<ProductInfo> products = products();
        ProductInfo _IPHONE_6S = products.get(0);
        ProductInfo _SGS7 = products.get(1);

        Basket basket = new Basket(null, products, null);
        ProductListDto productListDto = new ProductListDto(new Message("Some requested product has been delivered!", PARTIAL_SUCCESS), null, oneProductIsLess());
        Mockito.when(tradeEngineRestService.getProductList(Mockito.any())).thenReturn(productListDto);

        // when
        Order order = basketRestService.doShopping(basket);

        //then
        assertThat(order.getCustomerId()).isNull();
        assertThat(order.getTimeOfSale()).isNotNull();
        assertThat(order.getProductList()).containsExactly(_IPHONE_6S, _SGS7);
        assertThat(order.getAddress()).isNull();
        assertThat(order.getPrice()).isEqualTo(new Price(0, 0, 0, "PLN"));
        assertThat(order.getFlowResults()).hasSize(4).containsOnly(
                new Error(null, "", "Missing customer id!", ErrorType.MISSING_CUSTOMER_ID),
                new Error(null, "", "Missing address!", ErrorType.MISSING_ADDRESS),
                new Error(1L, _IPHONE_6S.getCommercialName(), "", ErrorType.NOT_ENOUGH_AMOUNT_OF_PRODUCT),
                new Error(2L, _SGS7.getCommercialName(), "", ErrorType.PRODUCT_IS_NOT_AVAILABLE)
        );
        assertThat(order.getGainedPoints()).isEqualTo(0L);
        assertThat(order.getDiscountList()).isEmpty();
        assertThat(order.getStatus()).isEqualTo(Message.Status.FAILURE);
    }

    @Test
    public void testExpectSuccess() {
        List<ProductInfo> products = products();
        ProductInfo _IPHONE_6S = products.get(0);
        ProductInfo _SGS7 = products.get(1);

        Basket basket = new Basket(customerId, products, address);
        ProductListDto productListDto = new ProductListDto(new Message("All requested product has been delivered!", SUCCESS), null, allProductsAreAvailable());
        Mockito.when(tradeEngineRestService.getProductList(Mockito.any())).thenReturn(productListDto);

        ShoppingHistoryDto shoppingHistoryDto = new ShoppingHistoryDto(new Message("Order has been added to shopping history!", Message.Status.SUCCESS), null);
        Mockito.when(shoppingHistoryRestService.addOrder(Mockito.any())).thenReturn(shoppingHistoryDto);

        // when
        Order order = basketRestService.doShopping(basket);

        //then
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getTimeOfSale()).isNotNull();
        assertThat(order.getProductList()).containsExactly(_IPHONE_6S, _SGS7);
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getPrice()).isEqualTo(new Price(42000, 11000, 53000, "PLN"));
        assertThat(order.getFlowResults()).isEmpty();
        assertThat(order.getGainedPoints()).isEqualTo(0L);
        assertThat(order.getDiscountList()).isEmpty();
        assertThat(order.getStatus()).isEqualTo(Message.Status.SUCCESS);
    }

    private List<ProductInfo> products() {
        ProductInfo IPHONE_6S = tradeEngineMapper.convertProduct(PRODUCT_IPHONE_6S);
        IPHONE_6S.setProductId(1L);
        IPHONE_6S.setQuantity(10);

        ProductInfo SGS7 = tradeEngineMapper.convertProduct(PRODUCT_SGS7);
        SGS7.setProductId(2L);
        SGS7.setQuantity(10);

        List<ProductInfo> products = asList(IPHONE_6S, SGS7);
        return products;
    }

    private List<ProductInfo> oneProductIsAvailable() {
        ProductInfo IPHONE_6S = tradeEngineMapper.convertProduct(PRODUCT_IPHONE_6S);
        IPHONE_6S.setProductId(1L);
        IPHONE_6S.setAvailable(true);
        IPHONE_6S.setQuantity(15);
        return asList(IPHONE_6S);
    }

    private List<ProductInfo> oneProductIsLess() {
        ProductInfo IPHONE_6S = tradeEngineMapper.convertProduct(PRODUCT_IPHONE_6S);
        IPHONE_6S.setProductId(1L);
        IPHONE_6S.setAvailable(true);
        IPHONE_6S.setQuantity(5);
        return asList(IPHONE_6S);
    }

    private List<ProductInfo> allProductsAreAvailable() {
        ProductInfo IPHONE_6S = tradeEngineMapper.convertProduct(PRODUCT_IPHONE_6S);
        IPHONE_6S.setProductId(1L);
        IPHONE_6S.setAvailable(true);
        IPHONE_6S.setQuantity(5);

        ProductInfo SGS7 = tradeEngineMapper.convertProduct(PRODUCT_SGS7);
        SGS7.setProductId(1L);
        SGS7.setAvailable(true);
        SGS7.setQuantity(5);
        return asList(IPHONE_6S, SGS7);
    }
}
