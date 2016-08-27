package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.SoldProductInfo;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.services.layers.BasketSupportLayer;
import com.tradeengine.common.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.tradeengine.common.Message.Status.FAILURE;

@Component
public class BasketRestService implements BasketSupportLayer {

    @Autowired
    private ShoppingHistoryRestService shoppingHistoryRestService;

    @Autowired
    private TradeEngineRestService tradeEngineRestService;

    @Override
    public boolean checkProductsAvailability(Basket basket) {
        ProductListDto requestedProducts = tradeEngineRestService.getProductList(
                basket.getProductList().stream()
                        .map(ProductInfo::getProductId)
                        .collect(Collectors.toList()));

        if (requestedProducts.getMessage().getStatus() == FAILURE ||
                requestedProducts.getProductList().size() < basket.getProductList().size()) {
            return false;
        }

//        return requestedProducts.getProductList().stream()
//                .allMatch(productInfo ->                    // sprawdz czy zamowienie nie przekraca dostepnosci produktu
//                        productInfo.isAvailable() && basket.getProductList(). productInfo.getQuantity());
        return false;
    }

    @Override
    public Order calculateOrder(Basket basket) {

        Price price = new Price(0, 0, 0, basket.getTargetCurrency());
        basket.getProductList().stream().forEach(productInfo ->
        {
            price.addAmount(productInfo.getQuantity() * productInfo.getPrice().getAmount());
            price.addTax(productInfo.getQuantity() * productInfo.getPrice().getTax());
        });


        return Order.builder()
                .customerId(basket.getCustomerId())
                .timeOfSale(basket.getTimeOfSale())
                .productList(basket.getProductList())
                .address(basket.getAddress())
                .price(price)
                .build();
    }

    @Override
    public Order calculatePoints(Order order) {
        // Will be implemented in future
        return order;
    }

    @Override
    public Order calculateDiscount(Order order) {
        // Will be implemented in future
        return order;
    }

    @Override
    public Order updateProductsAvailability(Order order) {
        // Will be implemented in future
        return null;
    }

    @Override
    public void processOrder(Order order) {
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

        shoppingHistoryRestService.addOrder(createCompletedOrderDto);
    }
    // procesowanie całego koszyka
    // sumowanie cen
    // naliczanie znizek (later)
    // zapisywanie zmian w bazie
}
