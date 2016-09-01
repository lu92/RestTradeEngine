package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.dto.SoldProductInfo;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngine.dto.RequestedProductsDto;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Error;
import com.tradeengine.TradeEngineAdapter.model.ErrorType;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.services.layers.BasketSupportLayer;
import com.tradeengine.common.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tradeengine.common.Message.Status.*;

@Component
public class BasketRestService extends BasketSupportLayer {

    @Autowired
    private ShoppingHistoryRestService shoppingHistoryRestService;

    @Autowired
    private TradeEngineRestService tradeEngineRestService;


    protected Order createOrder(Basket basket) {

        Order order = Order.builder()
                .customerId(basket.getCustomerId())
                .timeOfSale(LocalDateTime.now())
                .productList(basket.getProductList())
                .address(basket.getAddress())
                .price(new Price(0, 0, 0, "PLN"))
                .build();

        if (order.getCustomerId() == null) {
            order.getFlowResults().add(new Error(null, "", "Missing customer id!", ErrorType.MISSING_CUSTOMER_ID));
        }

        if (order.getAddress() == null) {
            order.getFlowResults().add(new Error(null, "", "Missing address!", ErrorType.MISSING_ADDRESS));
        }

        return order;
    }

    protected Order checkProductsAvailability(Order order) {

        List<Error> collectedErrors = new ArrayList<>();

        if (order.getProductList().isEmpty()) {
            order.setProductList(new ArrayList<>());
            order.addError(new Error(null, "", "Basket is Empty!", ErrorType.EMPTY_BASKET));
            order.setGainedPoints(0L);
            order.setDiscountList(new ArrayList<>());
            order.setStatus(FAILURE);
            return order;
        }

        ProductListDto requestedProducts = tradeEngineRestService.getProductList(
                new RequestedProductsDto(order.getProductList().stream()
                        .map(ProductInfo::getProductId)
                        .collect(Collectors.toList())));


        if (requestedProducts.getMessage().getStatus() == FAILURE) {
            List<Error> missingProducts = order.getProductList().stream()
                    .map(productInfo -> new Error(productInfo.getProductId(), productInfo.getCommercialName(), "", ErrorType.PRODUCT_IS_NOT_AVAILABLE))
                    .collect(Collectors.toList());

            collectedErrors.addAll(missingProducts);
        }

        if (requestedProducts.getMessage().getStatus() == PARTIAL_SUCCESS) {


            List<ProductInfo> missingProducts = order.getProductList().stream()
                    .filter(productInfo -> !requestedProducts.getProduct(productInfo.getProductId()).isPresent())
                    .collect(Collectors.toList());

            List<Error> productsWhichDoesNotOccur = missingProducts.stream()
                    .map(productInfo -> new Error(productInfo.getProductId(), productInfo.getCommercialName(), "", ErrorType.PRODUCT_IS_NOT_AVAILABLE))
                    .collect(Collectors.toList());

            List<Error> productsWhichAreNotEnough = order.getProductList().stream()
                    .filter(product -> !missingProducts.contains(product) && product.getQuantity() > requestedProducts.getProduct(product.getProductId()).get().getQuantity())
                    .map(productInfo -> new Error(productInfo.getProductId(), productInfo.getCommercialName(), "", ErrorType.NOT_ENOUGH_AMOUNT_OF_PRODUCT))
                    .collect(Collectors.toList());

            collectedErrors.addAll(productsWhichDoesNotOccur);
            collectedErrors.addAll(productsWhichAreNotEnough);
        }

        if (requestedProducts.getMessage().getStatus() == SUCCESS) {

            List<Error> productsWhichAreNotEnough = order.getProductList().stream()
                    .filter(product -> product.getQuantity() > requestedProducts.getProduct(product.getProductId()).get().getQuantity())
                    .map(productInfo -> new Error(productInfo.getProductId(), productInfo.getCommercialName(), "", ErrorType.NOT_ENOUGH_AMOUNT_OF_PRODUCT))
                    .collect(Collectors.toList());

            collectedErrors.addAll(productsWhichAreNotEnough);
        }

        order.getFlowResults().addAll(collectedErrors);
        order.setGainedPoints(0L);
        order.setDiscountList(new ArrayList<>());
        order.setStatus(FAILURE);

        return order;
    }


    protected Order calculateOrder(Order order) {
        order.getProductList().stream().forEach(productInfo ->
        {
            order.getPrice().addAmount(productInfo.getQuantity() * productInfo.getPrice().getAmount());
            order.getPrice().addTax(productInfo.getQuantity() * productInfo.getPrice().getTax());
        });
        return order;
    }

    protected Order calculateDiscount(Order order) {
        // Will be implemented in future
        return order;
    }

    protected Order UpdateCustomerStatus(Order order) {

        // zmienia ilosc pieniedzy i  tierlevel Customer'a
        return order;
    }

    protected Order updateProductsAvailability(Order order) {
        // Will be implemented in future
        return order;
    }

    protected Order processOrder(Order order) {
//        CustomerDto customer = profileReaderRestService.getCustomer(order.getCustomerId());

        CreateCompletedOrderDto createCompletedOrderDto = new CreateCompletedOrderDto(
                order.getCustomerId(),
                LocalDateTime.now(),
                order.getProductList().stream()
                        .map(productInfo -> SoldProductInfo.builder()
                                .productID(productInfo.getProductId())
                                .quantity(productInfo.getQuantity())
                                .price(productInfo.getPrice())
                                .build())
                        .collect(Collectors.toList()),
                order.getAddress(),
                order.getGainedPoints(),
                order.getPrice());

        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryRestService.addOrder(createCompletedOrderDto);
        if (shoppingHistoryDto.getMessage().getStatus() == FAILURE) {
            order.setStatus(FAILURE);
        }

        if (order.getFlowResults().isEmpty())
            order.setStatus(SUCCESS);
        else
            order.setStatus(FAILURE);

        return order;
    }
}
