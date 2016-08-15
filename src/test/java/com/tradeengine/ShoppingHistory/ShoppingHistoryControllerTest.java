package com.tradeengine.ShoppingHistory;

import com.tradeengine.ShoppingHistory.services.ShoppingHistoryServiceImpl;
import com.tradeengine.TestUtils;
import com.tradeengine.common.Message;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.tradeengine.ShoppingHistoryTestData.COMPLETED_ORDER_1;
import static com.tradeengine.ShoppingHistoryTestData.SHOPPING_HISTORY;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ShoppingHistoryTestContext.class })
@WebAppConfiguration
public class ShoppingHistoryControllerTest
{
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private WebApplicationContext webCtx;

    @Autowired
    private ShoppingHistoryServiceImpl shoppingHistoryService;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webCtx).build();
    }

    @Test
    public void testGetShoppingHistoryExpectSuccess() throws Exception
    {
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history has been delivered!", Message.Status.SUCCESS), SHOPPING_HISTORY);

        Mockito.when(shoppingHistoryService.getShoppingHistory(Mockito.any(Long.class))).thenReturn(SHOPPING_HISTORY_DTO);

        String RS = mockMvc.perform(get("/ShoppingHistory/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
                .andExpect(jsonPath("$.shoppingHistory.shoppingHistoryId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getShoppingHistoryId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.customerId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCustomerId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].orderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getTimeOfSale()))))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].orderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getTimeOfSale()))))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.totalAmount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.totalTaxes", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getTax())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetShoppingHistoryExpectFailure() throws Exception
    {
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);

        Mockito.when(shoppingHistoryService.getShoppingHistory(Mockito.any(Long.class))).thenReturn(SHOPPING_HISTORY_DTO);

        String RS = mockMvc.perform(get("/ShoppingHistory/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.FAILURE.toString())))
                .andExpect(jsonPath("$.shoppingHistory", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testCreateShoppingHistoryExpectSuccess() throws Exception
    {
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history has been created!", Message.Status.SUCCESS), SHOPPING_HISTORY);

        Mockito.when(shoppingHistoryService.createShoppingHistory(Mockito.any(Long.class))).thenReturn(SHOPPING_HISTORY_DTO);

        String RS = mockMvc.perform(post("/ShoppingHistory")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
                .andExpect(jsonPath("$.shoppingHistory.shoppingHistoryId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getShoppingHistoryId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.customerId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCustomerId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].orderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getTimeOfSale()))))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].orderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getTimeOfSale()))))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.totalAmount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.totalTaxes", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getTax())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testCreateShoppingHistoryExpectFailure() throws Exception
    {
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history already exists!", Message.Status.FAILURE), null);
        final long FAKE_CUSTOMER_ID = 1;

        Mockito.when(shoppingHistoryService.createShoppingHistory(Mockito.any(Long.class))).thenReturn(SHOPPING_HISTORY_DTO);

        String RS = mockMvc.perform(post("/ShoppingHistory")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(FAKE_CUSTOMER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.FAILURE.toString())))
                .andExpect(jsonPath("$.shoppingHistory", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddOrderWhenCustomerDoestExistExpectFailure() throws Exception
    {
        final long FAKE_CUSTOMER_ID = 1;
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Customer doesn't exist", Message.Status.FAILURE), null);

        Mockito.when(shoppingHistoryService.addOrder(Mockito.any(Long.class), Mockito.any())).thenReturn(SHOPPING_HISTORY_DTO);

        String content = TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1);
        logger.info("RQ = " + content);

        String RS = mockMvc.perform(post("/ShoppingHistory/" + FAKE_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.FAILURE.toString())))
                .andExpect(jsonPath("$.shoppingHistory", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddOrderWhenCustomerDoesNotHaveHistoryExpectFailure() throws Exception
    {
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);

        Mockito.when(shoppingHistoryService.addOrder(Mockito.any(Long.class), Mockito.any())).thenReturn(SHOPPING_HISTORY_DTO);
        String content = TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1);
        logger.info("RQ = " + content);

        String RS = mockMvc.perform(post("/ShoppingHistory/" + -1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.FAILURE.toString())))
                .andExpect(jsonPath("$.shoppingHistory", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testAddOrderExpectSuccess() throws Exception
    {
        final long PROPER_CUSTOMER_ID = 1;
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Order has been added to shopping history!", Message.Status.SUCCESS), SHOPPING_HISTORY);

        Mockito.when(shoppingHistoryService.addOrder(Mockito.any(Long.class), Mockito.any())).thenReturn(SHOPPING_HISTORY_DTO);

        String content = TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1);
        logger.info("RQ = " + content);

        String RS = mockMvc.perform(post("/ShoppingHistory/" + PROPER_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
                .andExpect(jsonPath("$.shoppingHistory.shoppingHistoryId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getShoppingHistoryId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.customerId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCustomerId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].orderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getTimeOfSale()))))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].orderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getTimeOfSale()))))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.cost", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].cost.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.totalAmount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.totalTaxes", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getTax())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }
}
