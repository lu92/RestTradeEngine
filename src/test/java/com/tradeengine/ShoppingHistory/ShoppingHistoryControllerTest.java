package com.tradeengine.ShoppingHistory;

import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryInfo;
import com.tradeengine.ShoppingHistory.mapper.ShoppingHistoryMapper;
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

    @Autowired
    private ShoppingHistoryMapper shoppingHistoryMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webCtx).build();
    }

    @Test
    public void testGetShoppingHistoryExpectSuccess() throws Exception
    {
        ShoppingHistoryInfo shoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(SHOPPING_HISTORY);
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history has been delivered!", Message.Status.SUCCESS), shoppingHistoryInfo);

        Mockito.when(shoppingHistoryService.getShoppingHistory(Mockito.any(Long.class))).thenReturn(SHOPPING_HISTORY_DTO);

        String RS = mockMvc.perform(get("/ShoppingHistory/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
//                .andExpect(jsonPath("$.shoppingHistory.shoppingHistoryId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getShoppingHistoryId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.customerId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCustomerId().intValue())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].completedOrderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getCompletedOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getTimeOfSale()))))
//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getCurrency())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.street", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getStreet())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.city", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getCity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.zipCode", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getZipCode())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.country", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getCountry())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].gainedPoints", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getGainedPoints().intValue())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].completedOrderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getCompletedOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getTimeOfSale()))))
//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getCurrency())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.street", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getStreet())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.city", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getCity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.zipCode", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getZipCode())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.country", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getCountry())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].gainedPoints", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getGainedPoints().intValue())))

                .andExpect(jsonPath("$.shoppingHistory.spendMoney.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.spendMoney.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getTax())))
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
        ShoppingHistoryInfo shoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(SHOPPING_HISTORY);
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Shopping history has been created!", Message.Status.SUCCESS), shoppingHistoryInfo);

        Mockito.when(shoppingHistoryService.createShoppingHistory(Mockito.any(Long.class))).thenReturn(SHOPPING_HISTORY_DTO);

        String RS = mockMvc.perform(post("/ShoppingHistory")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
//                .andExpect(jsonPath("$.shoppingHistory.shoppingHistoryId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getShoppingHistoryId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.customerId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCustomerId().intValue())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].completedOrderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getCompletedOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getTimeOfSale()))))
//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getCurrency())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.street", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getStreet())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.city", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getCity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.zipCode", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getZipCode())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.country", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getCountry())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].gainedPoints", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getGainedPoints().intValue())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].completedOrderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getCompletedOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getTimeOfSale()))))
//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getCurrency())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.street", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getStreet())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.city", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getCity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.zipCode", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getZipCode())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.country", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getCountry())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].gainedPoints", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getGainedPoints().intValue())))

                .andExpect(jsonPath("$.shoppingHistory.spendMoney.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.spendMoney.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getTax())))
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
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Customer doesn't exist", Message.Status.FAILURE), null);

        Mockito.when(shoppingHistoryService.addOrder(Mockito.any())).thenReturn(SHOPPING_HISTORY_DTO);

        String content = TestUtils.convertObjectToJsonText(COMPLETED_ORDER_1);
        logger.info("RQ = " + content);

        String RS = mockMvc.perform(post("/ShoppingHistory/Orders")
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
        ShoppingHistoryInfo shoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(SHOPPING_HISTORY);
        final ShoppingHistoryDto SHOPPING_HISTORY_DTO = new ShoppingHistoryDto(new Message("Order has been added to shopping history!", Message.Status.SUCCESS), shoppingHistoryInfo);

        final long PROPER_CUSTOMER_ID = 1;

        CreateCompletedOrderDto createCompletedOrderDto = shoppingHistoryMapper.mapCreateCompletedOrder(PROPER_CUSTOMER_ID, COMPLETED_ORDER_1);

        Mockito.when(shoppingHistoryService.addOrder(Mockito.any())).thenReturn(SHOPPING_HISTORY_DTO);

        String content = TestUtils.convertObjectToJsonText(createCompletedOrderDto);
        logger.info("RQ = " + content);

        String RS = mockMvc.perform(post("/ShoppingHistory/Orders")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(createCompletedOrderDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(SHOPPING_HISTORY_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
//                .andExpect(jsonPath("$.shoppingHistory.shoppingHistoryId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getShoppingHistoryId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.customerId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCustomerId().intValue())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].completedOrderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getCompletedOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getTimeOfSale()))))
//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[0].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(0).getPrice().getCurrency())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].soldProductsList[1].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.street", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getStreet())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.city", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getCity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.zipCode", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getZipCode())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].address.country", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getAddress().getCountry())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[0].gainedPoints", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(0).getGainedPoints().intValue())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].completedOrderId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getCompletedOrderId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].timeOfSale", equalTo(TestUtils.LocalDateTime2JSonFigure(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getTimeOfSale()))))
//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[0].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(0).getPrice().getCurrency())))

//                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].soldProductId", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getSoldProductId().intValue())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].quantity", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getQuantity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getTax())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.price", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getPrice())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].soldProductsList[1].price.currency", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getSoldProductsList().get(1).getPrice().getCurrency())))

                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.street", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getStreet())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.city", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getCity())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.zipCode", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getZipCode())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].address.country", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getAddress().getCountry())))
                .andExpect(jsonPath("$.shoppingHistory.completedOrderList[1].gainedPoints", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getCompletedOrderList().get(1).getGainedPoints().intValue())))

                .andExpect(jsonPath("$.shoppingHistory.spendMoney.amount", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getAmount())))
                .andExpect(jsonPath("$.shoppingHistory.spendMoney.tax", equalTo(SHOPPING_HISTORY_DTO.getShoppingHistory().getSpendMoney().getTax())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }
}
