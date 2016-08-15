package com.tradeengine.ShoppingHistory.services;

import com.tradeengine.MyMatchers;
import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.ShoppingHistory.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.ShoppingHistoryIntegrationContext;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.repositories.CompletedOrderRepository;
import com.tradeengine.ShoppingHistory.repositories.ShoppingHistoryRepository;

import static com.tradeengine.common.Message.Status.*;
import static org.fest.assertions.Assertions.*;

import com.tradeengine.ShoppingHistory.repositories.SoldProductRepository;
import com.tradeengine.common.entities.Price;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.tradeengine.ShoppingHistoryTestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ShoppingHistoryIntegrationContext.class})
@Rollback(true)
public class ShoppingHistoryServiceImplTest {
    @Autowired
    private ShoppingHistoryServiceImpl shoppingHistoryService;

    @Autowired
    private ShoppingHistoryRepository shoppingHistoryRepository;

    @Autowired
    private CompletedOrderRepository completedOrderRepository;

    @Autowired
    private SoldProductRepository soldProductRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    public void testCreateAndGetShoppingHistory() {
        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(1);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1L);

        ShoppingHistoryDto shoppingHistoryDto_GET = shoppingHistoryService.getShoppingHistory(shoppingHistoryDto_CREATE.getShoppingHistory().getShoppingHistoryId());
        assertThat(shoppingHistoryDto_GET.getMessage().getMessage()).isEqualTo("Shopping history has been delivered!");
        assertThat(shoppingHistoryDto_GET.getMessage().getStatus()).isEqualTo(SUCCESS);

        final ShoppingHistory EXPECTED_SHOPPING_HISTORY = ShoppingHistory.builder()
                .shoppingHistoryId(shoppingHistoryDto_GET.getShoppingHistory().getShoppingHistoryId())
                .customerId(1L)
                .spendMoney(Price.builder().amount(0).tax(0).build())
                .build();

        Assert.assertThat(shoppingHistoryDto_GET.getShoppingHistory(), MyMatchers.sameAs(EXPECTED_SHOPPING_HISTORY));
    }

    @Test
    @Transactional
    public void testGetShoppingHistoryWhenDoesntExistExpectFailure() {
        final int FAKE_CUSTOMER_ID = -1;
        ShoppingHistoryDto shoppingHistoryDto_GET = shoppingHistoryService.getShoppingHistory(FAKE_CUSTOMER_ID);
        assertThat(shoppingHistoryDto_GET.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto_GET.getMessage().getMessage()).isEqualTo("Shopping history doesn't exist for selected customer!");
        assertThat(shoppingHistoryDto_GET.getShoppingHistory()).isNull();
        assertThat(shoppingHistoryRepository.count()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void testTryToCreateTwoShoppingHistoryRelatedWithSameCustomerExpectFailure() {
        final int CUSTOMER_ID = 100;

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1L);

        ShoppingHistoryDto shoppingHistoryDto_CREATE_2 = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE_2.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto_CREATE_2.getMessage().getMessage()).isEqualTo("Shopping history already exists!");
        assertThat(shoppingHistoryDto_CREATE_2.getShoppingHistory()).isNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void testAddOrderWhenCustomerDoesNotExistExpectFailure() {
        CompletedOrder completedOrder = CompletedOrder.builder().build();
        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryService.addOrder(-1, completedOrder);
        assertThat(shoppingHistoryDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto.getMessage().getMessage()).isEqualTo("Shopping history doesn't exist for selected customer!");
        assertThat(shoppingHistoryDto.getShoppingHistory()).isNull();
    }

    @Test
    @Transactional
    public void testAddEmptyOrderExpectFailure() {
        final int CUSTOMER_ID = 100;

        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1L);

        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryService.addOrder(CUSTOMER_ID, EMPTY_COMPLETED_ORDER);
        assertThat(shoppingHistoryDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto.getMessage().getMessage()).isEqualTo("Delivered order doesn't contain any product's id!");
        assertThat(shoppingHistoryDto.getShoppingHistory()).isNull();
    }

    @Test
    @Transactional
    public void testAddOrderExpectSuccess() {
        final int CUSTOMER_ID = 100;

        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);

        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryService.addOrder(CUSTOMER_ID, COMPLETED_ORDER_1);
        assertThat(shoppingHistoryDto.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto.getMessage().getMessage()).isEqualTo("Order has been added to shopping history!");
        assertThat(shoppingHistoryDto.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);
        assertThat(completedOrderRepository.count()).isEqualTo(1);
        assertThat(soldProductRepository.count()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void testAddTwoOrdersExpectSuccess() {
        final int CUSTOMER_ID = 100;

        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);

        ShoppingHistoryDto shoppingHistoryDto_1 = shoppingHistoryService.addOrder(CUSTOMER_ID, COMPLETED_ORDER_1);
        assertThat(shoppingHistoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_1.getMessage().getMessage()).isEqualTo("Order has been added to shopping history!");
        assertThat(shoppingHistoryDto_1.getShoppingHistory()).isNotNull();

        ShoppingHistoryDto shoppingHistoryDto_2 = shoppingHistoryService.addOrder(CUSTOMER_ID, COMPLETED_ORDER_2);
        assertThat(shoppingHistoryDto_2.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_2.getMessage().getMessage()).isEqualTo("Order has been added to shopping history!");
        assertThat(shoppingHistoryDto_2.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);
        assertThat(completedOrderRepository.count()).isEqualTo(2);
        assertThat(soldProductRepository.count()).isEqualTo(4);
    }
}
