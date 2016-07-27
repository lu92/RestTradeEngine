package com.tradeengine;

import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.common.entities.Price;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.entities.SoldProduct;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

public class ShoppingHistoryTestData
{
    public static final long CUSTOMER_ID = 1;
    public static final long SHOPPING_HISTORY_ID = 1;
    public static final long ORDER_ID_1 = 1L;
    public static final long ORDER_ID_2 = 2L;
    public static final LocalDateTime TIME_OF_SALE = LocalDateTime.of(2016, Month.JANUARY, 1, 5, 10, 30);
    public static final LocalDateTime TIME_OF_SALE_2 = LocalDateTime.of(2016, Month.JANUARY, 2, 5, 10, 30);
    public static final LocalDateTime TIME_OF_SALE_EMPTY_ORDER = LocalDateTime.of(2016, Month.JANUARY, 3, 5, 10, 30);
    public static final int TOTAL_AMOUNT = 150;
    public static final double TOTAL_TAXES = 34.5;

    public static final long PRODUCT_ID_1 = 1L;
    public static final long PRODUCT_ID_2 = 2L;
    public static final long PRODUCT_ID_3 = 3L;
    public static final long PRODUCT_ID_4 = 4L;

    public static final String CURRENCY_PLN = "PLN";

    public static final SoldProduct SOLD_PRODUCT_1 = SoldProduct.builder()
            .productId(PRODUCT_ID_1)
            .quantity(1)
            .price(Price.builder().amount(10).tax(2.30).price(12.30).currency(CURRENCY_PLN).build())
            .build();

    public static final SoldProduct SOLD_PRODUCT_2 = SoldProduct.builder()
            .productId(PRODUCT_ID_2)
            .quantity(1)
            .price(Price.builder() .amount(20).tax(4.60).price(24.60).currency(CURRENCY_PLN).build())
            .build();

    public static final SoldProduct SOLD_PRODUCT_3 = SoldProduct.builder()
            .productId(PRODUCT_ID_3)
            .quantity(1)
            .price(Price.builder().amount(40).tax(9.20).price(49.20).currency(CURRENCY_PLN).build())
            .build();

    public static final SoldProduct SOLD_PRODUCT_4 = SoldProduct.builder()
            .productId(PRODUCT_ID_4)
            .quantity(1)
            .price(Price.builder().amount(80).tax(18.40).price(98.40).currency(CURRENCY_PLN).build())
            .build();

    public static final CompletedOrder COMPLETED_ORDER_1 = CompletedOrder.builder()
            .orderId(ORDER_ID_1)
            .timeOfSale(TIME_OF_SALE)
            .soldProductsList(Arrays.asList(SOLD_PRODUCT_1, SOLD_PRODUCT_2))
            .price(Price.builder().amount(30).tax(6.90).price(36.90).currency("USD").build())
            .build();

    public static final CompletedOrder COMPLETED_ORDER_2 = CompletedOrder.builder()
            .orderId(ORDER_ID_2)
            .timeOfSale(TIME_OF_SALE_2)
            .soldProductsList(Arrays.asList(SOLD_PRODUCT_3, SOLD_PRODUCT_4))
            .price(Price.builder().amount(120).tax(55.30).price(175.30).currency("USD").build())
            .build();

    public static final CompletedOrder EMPTY_COMPLETED_ORDER = CompletedOrder.builder()
            .orderId(ORDER_ID_2)
            .timeOfSale(TIME_OF_SALE_EMPTY_ORDER)
            .price(Price.builder().amount(0).tax(0).price(0).currency("USD").build())
            .build();

    public static final ShoppingHistory SHOPPING_HISTORY = ShoppingHistory.builder()
            .shoppingHistoryId(SHOPPING_HISTORY_ID)
            .customerId(CUSTOMER_ID)
            .completedOrderList(Arrays.asList(COMPLETED_ORDER_1, COMPLETED_ORDER_2))
            .totalAmount(TOTAL_AMOUNT)
            .totalTaxes(TOTAL_TAXES)
            .build();
}
