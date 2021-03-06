package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.DynamicRetailer.entities.RuleType;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.dto.SoldProductInfo;
import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngine.dto.RequestedProductsDto;
import com.tradeengine.TradeEngine.mappers.TradeEngineMapper;
import com.tradeengine.TradeEngineAdapter.exceptions.*;
import com.tradeengine.TradeEngineAdapter.model.*;
import com.tradeengine.TradeEngineAdapter.model.Error;
import com.tradeengine.TradeEngineAdapter.services.layers.BasketSupportLayer;
import com.tradeengine.common.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.tradeengine.DynamicRetailer.entities.RuleType.CUSTOMER_STATUS_CHANGED;
import static com.tradeengine.common.Message.Status.*;

@Component
public class BasketRestService implements BasketSupportLayer {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private ProfileReaderRestService profileReaderRestService;
    private TradeEngineRestService tradeEngineRestService;
    private ShoppingHistoryRestService shoppingHistoryRestService;
    private DynamicRetailerRestService dynamicRetailerRestService;


    @Autowired
    public BasketRestService(ProfileReaderRestService profileReaderRestService,
                             TradeEngineRestService tradeEngineRestService,
                             ShoppingHistoryRestService shoppingHistoryRestService,
                             DynamicRetailerRestService dynamicRetailerRestService) {
        this.profileReaderRestService = profileReaderRestService;
        this.tradeEngineRestService = tradeEngineRestService;
        this.shoppingHistoryRestService = shoppingHistoryRestService;
        this.dynamicRetailerRestService = dynamicRetailerRestService;
    }

    public Order createOrder(Basket basket) throws InvalidBasketException {

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

        if (basket.getProductList().isEmpty()) {
            order.addError(new Error(null, "", "Basket is Empty!", ErrorType.EMPTY_BASKET));
        }

        if (!order.getFlowResults().isEmpty()) {
            order.setGainedPoints(0);
            order.setDiscountList(new ArrayList<>());
            order.setStatus(FAILURE);
            throw new InvalidBasketException(order);
        }

        return order;
    }

    public Order checkProductsAvailability(Order order) throws ProductsAvailabilityException {

        List<Error> collectedErrors = new ArrayList<>();

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

        if (!collectedErrors.isEmpty()) {
            order.getFlowResults().addAll(collectedErrors);
            order.setGainedPoints(0);
            order.setDiscountList(new ArrayList<>());
            order.setStatus(FAILURE);
            throw new ProductsAvailabilityException(order);
        }

        return order;
    }


    public Order calculateOrder(Order order) {
        order.getProductList().stream().forEach(productInfo ->
        {
            order.getPrice().addAmount(productInfo.getQuantity() * productInfo.getPrice().getAmount());
            order.getPrice().addTax(productInfo.getQuantity() * productInfo.getPrice().getTax());
        });
        return order;
    }

    public Order calculateDiscount(Order order) {
        // Will be implemented in future
        Order orderAfterDiscount = dynamicRetailerRestService.calculateDiscount(order);
//        order.setGainedPoints(0L);
        return orderAfterDiscount;
    }

    @Override
    public Order checkAccountBalance(Order order) throws NotEnoughAccountBalanceException {
        CustomerDto customer = profileReaderRestService.getCustomer(order.getCustomerId());
        if (customer.getCustomer().getCreditCard().getBalance() < order.getPrice().getPrice()) {
            throw new NotEnoughAccountBalanceException();
        }

        return order;
    }

    @Override
    public Order updateCustomerStatus(Order order) {
        // zmienia ilosc pieniedzy i  tierlevel Customer'a
        CustomerDto customer = profileReaderRestService.getCustomer(order.getCustomerId());
        customer.getCustomer().getCreditCard().decreaseBalance(order.getPrice().getPrice());

        final Optional<Discount> tierLevelChangedDiscount = order.getDiscountList().stream()
                .filter(discount -> discount.getRuleType() == CUSTOMER_STATUS_CHANGED).findFirst();

        // if tierlevel has changed
        if (tierLevelChangedDiscount.isPresent() && customer.getCustomer().getTierLevel().getWeight() > tierLevelChangedDiscount.get().getUpgradedTierLevel().getWeight()) {
            customer.getCustomer().setTierLevel(tierLevelChangedDiscount.get().getUpgradedTierLevel());
        }

        // if gainedPoints has changed
        if (order.getGainedPoints() > 0) {
            customer.getCustomer().setPoints(customer.getCustomer().getPoints() + order.getGainedPoints());
        }

        profileReaderRestService.updateCustomer(customer.getCustomer());
        return order;
    }

    public Order updateProductsAvailability(Order order) {
        ProductListDto productList = tradeEngineRestService.getProductList(new RequestedProductsDto(order.getProductList().stream()
                .map(ProductInfo::getProductId)
                .collect(Collectors.toList())));

        for (ProductInfo productInfo : productList.getProductList()) {
            int newQuantity = productInfo.getQuantity() - order.getProduct(productInfo.getProductId()).get().getQuantity();
            tradeEngineRestService.updateProductQuantity(
                    productInfo.getProductId(),newQuantity);

            if (newQuantity == 0)
                tradeEngineRestService.deactivateProduct(productInfo.getProductId());
        }
        return order;
    }

    public Order processOrder(Order order) {
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

    public Order doShopping(Basket basket) {
        Order order = null;
//        = Order.builder()
////                .customerId(basket.getCustomerId())
//                .timeOfSale(LocalDateTime.now())
////                .productList(basket.getProductList())
////                .address(basket.getAddress())
//                .price(new Price(0, 0, 0, "PLN"))
//                .build();
        try {
            order = createOrder(basket);
            checkProductsAvailability(order);
            calculateOrder(order);
            order = calculateDiscount(order);
            checkAccountBalance(order);
            updateProductsAvailability(order);
            updateCustomerStatus(order);
            processOrder(order);
        } catch (InvalidBasketException e) {
            order = e.getOrder();

        } catch (ProductsAvailabilityException e) {

        } catch (NotEnoughAccountBalanceException e) {

        }

        return order;

    }
}
