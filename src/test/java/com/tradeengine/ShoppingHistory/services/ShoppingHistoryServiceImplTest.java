package com.tradeengine.ShoppingHistory.services;

import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.ShoppingHistory.ShoppingHistoryIntegrationContext;
import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.mapper.ShoppingHistoryMapper;
import com.tradeengine.ShoppingHistory.repositories.CompletedOrderRepository;
import com.tradeengine.ShoppingHistory.repositories.ShoppingHistoryRepository;
import com.tradeengine.ShoppingHistory.repositories.SoldProductRepository;
import com.tradeengine.common.entities.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tradeengine.ShoppingHistoryTestData.*;
import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;
import static org.fest.assertions.Assertions.assertThat;

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

    @Autowired
    private ShoppingHistoryMapper shoppingHistoryMapper;


    @Before
    public void cleanDb() {
        SHOPPING_HISTORY.setCustomerId(null);
        COMPLETED_ORDER_1.setOrderId(null);
        COMPLETED_ORDER_2.setOrderId(null);
        SOLD_PRODUCT_1.setSoldProductId(null);
        SOLD_PRODUCT_2.setSoldProductId(null);
        SOLD_PRODUCT_3.setSoldProductId(null);
        SOLD_PRODUCT_4.setSoldProductId(null);

        soldProductRepository.deleteAll();
        completedOrderRepository.deleteAll();
        shoppingHistoryRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndGetShoppingHistory() {
        final long CUSTOMER_ID = 100;
        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        List<ShoppingHistory> all = shoppingHistoryRepository.findAll();
        assertThat(all.size()).isEqualTo(1);

        ShoppingHistoryDto shoppingHistoryDto_GET = shoppingHistoryService.getShoppingHistory(shoppingHistoryDto_CREATE.getShoppingHistory().getCustomerId());
        assertThat(shoppingHistoryDto_GET.getMessage().getMessage()).isEqualTo("Shopping history has been delivered!");
        assertThat(shoppingHistoryDto_GET.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_GET.getShoppingHistory().getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_GET.getShoppingHistory().getSpendMoney()).isEqualTo(Price.builder().amount(0).tax(0).price(0).currency("PLN").build());
        assertThat(shoppingHistoryDto_GET.getShoppingHistory().getCompletedOrderList()).isEmpty();
        assertThat(shoppingHistoryDto_GET.getShoppingHistory().getShoppingHistoryId()).isEqualTo(shoppingHistoryDto_CREATE.getShoppingHistory().getShoppingHistoryId());
    }

    @Test
    @Transactional
    public void testGetShoppingHistoryWhenDoesntExistExpectFailure() {
        final long FAKE_CUSTOMER_ID = -1;
        ShoppingHistoryDto shoppingHistoryDto_GET = shoppingHistoryService.getShoppingHistory(FAKE_CUSTOMER_ID);
        assertThat(shoppingHistoryDto_GET.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto_GET.getMessage().getMessage()).isEqualTo("Shopping history doesn't exist for selected customer!");
        assertThat(shoppingHistoryDto_GET.getShoppingHistory()).isNull();
        assertThat(shoppingHistoryRepository.count()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void testTryToCreateTwoShoppingHistoryRelatedWithSameCustomerExpectFailure() {
        final long CUSTOMER_ID = 100;

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
        final long CUSTOMER_ID = 100;
        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(false);

        CreateCompletedOrderDto createCompletedOrderDto = new CreateCompletedOrderDto();
        createCompletedOrderDto.setCustomerId(CUSTOMER_ID);
        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryService.addOrder(createCompletedOrderDto);
        assertThat(shoppingHistoryDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto.getMessage().getMessage()).isEqualTo("Customer doesn't exist");
        assertThat(shoppingHistoryDto.getShoppingHistory()).isNull();
    }

    @Test
    @Transactional
    public void testAddEmptyOrderExpectFailure() {
        final long CUSTOMER_ID = 100;

        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1L);

        CreateCompletedOrderDto createCompletedOrderDto = new CreateCompletedOrderDto();
        createCompletedOrderDto.setCustomerId(CUSTOMER_ID);

        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryService.addOrder(createCompletedOrderDto);
        assertThat(shoppingHistoryDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(shoppingHistoryDto.getMessage().getMessage()).isEqualTo("Delivered order doesn't contain any sold products!");
        assertThat(shoppingHistoryDto.getShoppingHistory()).isNull();
    }

    @Test
    @Transactional
    public void testAddOrderExpectSuccess() {
        final long CUSTOMER_ID = 100;

        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory().getSpendMoney().getAmount()).isEqualTo(0);
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory().getSpendMoney().getTax()).isEqualTo(0);
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory().getSpendMoney().getPrice()).isEqualTo(0);
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory().getSpendMoney().getCurrency()).isEqualTo("PLN");


        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);
        CreateCompletedOrderDto createCompletedOrderDto = shoppingHistoryMapper.mapCreateCompletedOrder(CUSTOMER_ID, COMPLETED_ORDER_1);

        ShoppingHistoryDto shoppingHistoryDto = shoppingHistoryService.addOrder(createCompletedOrderDto);
        assertThat(shoppingHistoryDto.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto.getMessage().getMessage()).isEqualTo("Order has been added to shopping history!");
        assertThat(shoppingHistoryDto.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);
        assertThat(completedOrderRepository.count()).isEqualTo(1);
        assertThat(soldProductRepository.count()).isEqualTo(2);

        assertThat(shoppingHistoryDto.getShoppingHistory().getSpendMoney().getAmount()).isEqualTo(COMPLETED_ORDER_1.getCost().getAmount());
        assertThat(shoppingHistoryDto.getShoppingHistory().getSpendMoney().getTax()).isEqualTo(COMPLETED_ORDER_1.getCost().getTax());
        assertThat(shoppingHistoryDto.getShoppingHistory().getSpendMoney().getPrice()).isEqualTo(COMPLETED_ORDER_1.getCost().getPrice());
        assertThat(shoppingHistoryDto.getShoppingHistory().getSpendMoney().getCurrency()).isEqualTo("PLN");
    }

    @Test
    @Transactional
    public void testAddTwoOrdersExpectSuccess() {
        final long CUSTOMER_ID = 100;

        Mockito.when(customerRepository.exists(Mockito.anyLong())).thenReturn(true);

        ShoppingHistoryDto shoppingHistoryDto_CREATE = shoppingHistoryService.createShoppingHistory(CUSTOMER_ID);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_CREATE.getMessage().getMessage()).isEqualTo("Shopping history has been created!");
        assertThat(shoppingHistoryDto_CREATE.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);

        CreateCompletedOrderDto createCompletedOrderDto1 = shoppingHistoryMapper.mapCreateCompletedOrder(CUSTOMER_ID, COMPLETED_ORDER_1);
        ShoppingHistoryDto shoppingHistoryDto_1 = shoppingHistoryService.addOrder(createCompletedOrderDto1);
        assertThat(shoppingHistoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_1.getMessage().getMessage()).isEqualTo("Order has been added to shopping history!");
        assertThat(shoppingHistoryDto_1.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryDto_1.getShoppingHistory().getSpendMoney().getAmount()).isEqualTo(COMPLETED_ORDER_1.getCost().getAmount());
        assertThat(shoppingHistoryDto_1.getShoppingHistory().getSpendMoney().getTax()).isEqualTo(COMPLETED_ORDER_1.getCost().getTax());
        assertThat(shoppingHistoryDto_1.getShoppingHistory().getSpendMoney().getPrice()).isEqualTo(COMPLETED_ORDER_1.getCost().getPrice());
        assertThat(shoppingHistoryDto_1.getShoppingHistory().getSpendMoney().getCurrency()).isEqualTo("PLN");

        CreateCompletedOrderDto createCompletedOrderDto2 = shoppingHistoryMapper.mapCreateCompletedOrder(CUSTOMER_ID, COMPLETED_ORDER_2);
        ShoppingHistoryDto shoppingHistoryDto_2 = shoppingHistoryService.addOrder(createCompletedOrderDto2);
        assertThat(shoppingHistoryDto_2.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(shoppingHistoryDto_2.getMessage().getMessage()).isEqualTo("Order has been added to shopping history!");
        assertThat(shoppingHistoryDto_2.getShoppingHistory()).isNotNull();

        assertThat(shoppingHistoryRepository.count()).isEqualTo(1);
        assertThat(completedOrderRepository.count()).isEqualTo(2);
        assertThat(soldProductRepository.count()).isEqualTo(4);

        assertThat(shoppingHistoryDto_2.getShoppingHistory().getSpendMoney().getAmount()).isEqualTo(COMPLETED_ORDER_1.getCost().getAmount() + COMPLETED_ORDER_2.getCost().getAmount());
        assertThat(shoppingHistoryDto_2.getShoppingHistory().getSpendMoney().getTax()).isEqualTo(COMPLETED_ORDER_1.getCost().getTax() + COMPLETED_ORDER_2.getCost().getTax());
        assertThat(shoppingHistoryDto_2.getShoppingHistory().getSpendMoney().getPrice()).isEqualTo(COMPLETED_ORDER_1.getCost().getPrice() + COMPLETED_ORDER_2.getCost().getPrice());
        assertThat(shoppingHistoryDto_2.getShoppingHistory().getSpendMoney().getCurrency()).isEqualTo("PLN");
    }
}
