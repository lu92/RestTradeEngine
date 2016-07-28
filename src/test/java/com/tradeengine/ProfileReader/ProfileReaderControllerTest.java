package com.tradeengine.ProfileReader;

import com.tradeengine.ProfileReader.services.ProfileReaderService;
import com.tradeengine.TestUtils;
import com.tradeengine.common.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import static com.tradeengine.ProfileReaderTestData.CREATE_CUSTOMER_DTO;
import static com.tradeengine.ProfileReaderTestData.CUSTOMER;
import static com.tradeengine.ProfileReaderTestData.CUSTOMER_2;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestContext.class })
@WebAppConfiguration
public class ProfileReaderControllerTest
{
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private WebApplicationContext webCtx;

    @Autowired
    private ProfileReaderService profileReaderServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webCtx).build();
    }

    @Test
    public void testGetCustomerWhenExist() throws Exception
    {
        CUSTOMER.setCustomerId(1L);
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer has been delivered!", Message.Status.SUCCESS), CUSTOMER);
        Mockito.when(profileReaderServiceMock.getCustomer(Mockito.any(Long.class))).thenReturn(CUSTOMER_DTO);

        String RSAsString = mockMvc.perform(get("/ProfileReader/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(jsonPath("$.message.message", equalTo("customer has been delivered!")))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
                .andExpect(jsonPath("$.customer.customerId", is(1)))
                .andExpect(jsonPath("$.customer.firstname", is(CUSTOMER.getFirstname())))
                .andExpect(jsonPath("$.customer.lastname", is(CUSTOMER.getLastname())))
                .andExpect(jsonPath("$.customer.email", is(CUSTOMER.getEmail())))
                //                .andExpect(jsonPath("$.customer.birthday", is("")))
                .andExpect(jsonPath("$.customer.address.street", is(CUSTOMER.getAddress().getStreet())))
                .andExpect(jsonPath("$.customer.address.city", is(CUSTOMER.getAddress().getCity())))
                .andExpect(jsonPath("$.customer.address.zipCode", is(CUSTOMER.getAddress().getZipCode())))
                .andExpect(jsonPath("$.customer.address.country", is(CUSTOMER.getAddress().getCountry())))
                .andExpect(jsonPath("$.customer.creditCard.number", is(CUSTOMER.getCreditCard().getNumber())))
                .andExpect(jsonPath("$.customer.creditCard.balance", is(CUSTOMER.getCreditCard().getBalance())))
                .andExpect(jsonPath("$.customer.creditCard.currency", is(CUSTOMER.getCreditCard().getCurrency())))
                //                .andExpect(jsonPath("$.customer.creationDate", is(CUSTOMER.getEmail())))
                .andExpect(jsonPath("$.customer.points", is(CUSTOMER.getPoints().intValue())))
                .andExpect(jsonPath("$.customer.tierLevel", is(CUSTOMER.getTierLevel().toString())))
                .andReturn().getResponse().getContentAsString();
        logger.info("RS = " + RSAsString);
    }

    @Test
    public void testGetCustomerWhenDoesNotExist() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer doesn't exist!", Message.Status.FAILURE), null);
        Mockito.when(profileReaderServiceMock.getCustomer(Mockito.any(Long.class))).thenReturn(CUSTOMER_DTO);

        String RS = mockMvc.perform(get("/ProfileReader/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("customer doesn't exist!")))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.FAILURE.toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testGetCustomerListWhenEmpty() throws Exception
    {
        final CustomerDtoList emptyCustomerDtoList = new CustomerDtoList(new Message("Customer's list is empty", Message.Status.SUCCESS), new ArrayList<>());
        Mockito.when(profileReaderServiceMock.getCustomerList()).thenReturn(emptyCustomerDtoList);

        mockMvc.perform(get("/ProfileReader"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Customer's list is empty")))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
                .andExpect(jsonPath("$.customerList", hasSize(0)));
    }

    @Test
    public void testGetCustomerListWhenFilled() throws Exception
    {
        final CustomerDtoList customerDtoList = new CustomerDtoList(new Message("Customer's list is filled", Message.Status.SUCCESS), Arrays.asList(CUSTOMER, CUSTOMER_2));
        Mockito.when(profileReaderServiceMock.getCustomerList()).thenReturn(customerDtoList);

        String RS = mockMvc.perform(get("/ProfileReader"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo("Customer's list is filled")))
                .andExpect(jsonPath("$.message.status", equalTo(Message.Status.SUCCESS.toString())))
                .andExpect(jsonPath("$.customerList", hasSize(2)))
                .andExpect(jsonPath("$.customerList[0].firstname", is(CUSTOMER.getFirstname())))
                .andExpect(jsonPath("$.customerList[0].lastname", is(CUSTOMER.getLastname())))
                .andExpect(jsonPath("$.customerList[0].email", is(CUSTOMER.getEmail())))
                .andExpect(jsonPath("$.customerList[0].address.street", is(CUSTOMER.getAddress().getStreet())))
                .andExpect(jsonPath("$.customerList[0].address.city", is(CUSTOMER.getAddress().getCity())))
                .andExpect(jsonPath("$.customerList[0].address.zipCode", is(CUSTOMER.getAddress().getZipCode())))
                .andExpect(jsonPath("$.customerList[0].address.country", is(CUSTOMER.getAddress().getCountry())))
                .andExpect(jsonPath("$.customerList[0].creditCard.number", is(CUSTOMER.getCreditCard().getNumber())))
                .andExpect(jsonPath("$.customerList[0].creditCard.balance", is(CUSTOMER.getCreditCard().getBalance())))
                .andExpect(jsonPath("$.customerList[0].creditCard.currency", is(CUSTOMER.getCreditCard().getCurrency())))
                .andExpect(jsonPath("$.customerList[0].points", is(CUSTOMER.getPoints().intValue())))
                .andExpect(jsonPath("$.customerList[0].tierLevel", is(CUSTOMER.getTierLevel().toString())))
                .andExpect(jsonPath("$.customerList[1].firstname", is(CUSTOMER_2.getFirstname())))
                .andExpect(jsonPath("$.customerList[1].lastname", is(CUSTOMER_2.getLastname())))
                .andExpect(jsonPath("$.customerList[1].email", is(CUSTOMER_2.getEmail())))
                .andExpect(jsonPath("$.customerList[1].address.street", is(CUSTOMER_2.getAddress().getStreet())))
                .andExpect(jsonPath("$.customerList[1].address.city", is(CUSTOMER_2.getAddress().getCity())))
                .andExpect(jsonPath("$.customerList[1].address.zipCode", is(CUSTOMER_2.getAddress().getZipCode())))
                .andExpect(jsonPath("$.customerList[1].address.country", is(CUSTOMER_2.getAddress().getCountry())))
                .andExpect(jsonPath("$.customerList[1].creditCard.number", is(CUSTOMER_2.getCreditCard().getNumber())))
                .andExpect(jsonPath("$.customerList[1].creditCard.balance", is(CUSTOMER_2.getCreditCard().getBalance())))
                .andExpect(jsonPath("$.customerList[1].creditCard.currency", is(CUSTOMER_2.getCreditCard().getCurrency())))
                .andExpect(jsonPath("$.customerList[1].points", is(CUSTOMER_2.getPoints().intValue())))
                .andExpect(jsonPath("$.customerList[1].tierLevel", is(CUSTOMER_2.getTierLevel().toString())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testCreateCustomerExpectSuccess() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer has been added!", Message.Status.SUCCESS), CUSTOMER);
        Mockito.when(profileReaderServiceMock.createCustomer(CREATE_CUSTOMER_DTO)).thenReturn(CUSTOMER_DTO);

        String RQ = TestUtils.convertObjectToJsonText(CREATE_CUSTOMER_DTO);
        logger.info("RQ = " + RQ);

        String RS = mockMvc.perform(post("/ProfileReader")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(CREATE_CUSTOMER_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer.firstname", is(CUSTOMER.getFirstname())))
                .andExpect(jsonPath("$.customer.lastname", is(CUSTOMER.getLastname())))
                .andExpect(jsonPath("$.customer.email", is(CUSTOMER.getEmail())))
                .andExpect(jsonPath("$.customer.address.street", is(CUSTOMER.getAddress().getStreet())))
                .andExpect(jsonPath("$.customer.address.city", is(CUSTOMER.getAddress().getCity())))
                .andExpect(jsonPath("$.customer.address.zipCode", is(CUSTOMER.getAddress().getZipCode())))
                .andExpect(jsonPath("$.customer.address.country", is(CUSTOMER.getAddress().getCountry())))
                .andExpect(jsonPath("$.customer.creditCard.number", is(CUSTOMER.getCreditCard().getNumber())))
                .andExpect(jsonPath("$.customer.creditCard.balance", is(CUSTOMER.getCreditCard().getBalance())))
                .andExpect(jsonPath("$.customer.creditCard.currency", is(CUSTOMER.getCreditCard().getCurrency())))
                .andExpect(jsonPath("$.customer.points", is(CUSTOMER.getPoints().intValue())))
                .andExpect(jsonPath("$.customer.tierLevel", is(CUSTOMER.getTierLevel().toString())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testCreateCustomerExpectFailure() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer already exists!", Message.Status.FAILURE), null);
        Mockito.when(profileReaderServiceMock.createCustomer(CREATE_CUSTOMER_DTO)).thenReturn(CUSTOMER_DTO);

        String RQ = TestUtils.convertObjectToJsonText(CREATE_CUSTOMER_DTO);
        logger.info("RQ = " + RQ);

        String RS = mockMvc.perform(post("/ProfileReader")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testDeleteCustomerExpectSuccess() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer has been deleted!", Message.Status.SUCCESS), null);
        Mockito.when(profileReaderServiceMock.deleteCustomer(Matchers.anyLong())).thenReturn(CUSTOMER_DTO);

        String RS = mockMvc.perform(delete("/ProfileReader/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testDeleteCustomerExpectFailure() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer doesn't exist!", Message.Status.FAILURE), null);
        Mockito.when(profileReaderServiceMock.deleteCustomer(Matchers.anyLong())).thenReturn(CUSTOMER_DTO);

        String RS = mockMvc.perform(delete("/ProfileReader/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testUpdateCustomerExpectSuccess() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customer has been updated!", Message.Status.SUCCESS), CUSTOMER_2);
        Mockito.when(profileReaderServiceMock.updateCustomer(CUSTOMER)).thenReturn(CUSTOMER_DTO);

        String RS = mockMvc.perform(put("/ProfileReader")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer.firstname", is(CUSTOMER_2.getFirstname())))
                .andExpect(jsonPath("$.customer.lastname", is(CUSTOMER_2.getLastname())))
                .andExpect(jsonPath("$.customer.email", is(CUSTOMER_2.getEmail())))
                .andExpect(jsonPath("$.customer.address.street", is(CUSTOMER_2.getAddress().getStreet())))
                .andExpect(jsonPath("$.customer.address.city", is(CUSTOMER_2.getAddress().getCity())))
                .andExpect(jsonPath("$.customer.address.zipCode", is(CUSTOMER_2.getAddress().getZipCode())))
                .andExpect(jsonPath("$.customer.address.country", is(CUSTOMER_2.getAddress().getCountry())))
                .andExpect(jsonPath("$.customer.creditCard.number", is(CUSTOMER_2.getCreditCard().getNumber())))
                .andExpect(jsonPath("$.customer.creditCard.balance", is(CUSTOMER_2.getCreditCard().getBalance())))
                .andExpect(jsonPath("$.customer.creditCard.currency", is(CUSTOMER_2.getCreditCard().getCurrency())))
                .andExpect(jsonPath("$.customer.points", is(CUSTOMER_2.getPoints().intValue())))
                .andExpect(jsonPath("$.customer.tierLevel", is(CUSTOMER_2.getTierLevel().toString())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testUpdateCustomerExpectFailure() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("can't update because of customer doesn't exist!", Message.Status.FAILURE), null);
        Mockito.when(profileReaderServiceMock.updateCustomer(CUSTOMER)).thenReturn(CUSTOMER_DTO);

        String RS = mockMvc.perform(put("/ProfileReader")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testUpdateCustomerExpectFailureBecauseOfMissingCustomerId() throws Exception
    {
        final CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("customerId is demanded!", Message.Status.FAILURE), null);
        Mockito.when(profileReaderServiceMock.updateCustomer(CUSTOMER)).thenReturn(CUSTOMER_DTO);

        String RQ = TestUtils.convertObjectToJsonText(CUSTOMER);

        logger.info("RQ = " + RQ);

        String RS = mockMvc.perform(put("/ProfileReader")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testLoginCustomerWhenExistExpectSuccess() throws Exception
    {
        String username = "USERNAME";
        String password = "PASSWORD";
        final LoginDto loginDto = new LoginDto(username, password);

        CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("ACCESS GRANTED!", Message.Status.SUCCESS), CUSTOMER);

        Mockito.when(profileReaderServiceMock.login(username, password)).thenReturn(CUSTOMER_DTO);

        String RQ = TestUtils.convertObjectToJsonText(loginDto);

        logger.info("RQ = " + RQ);

        String RS = mockMvc.perform(post("/ProfileReader/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer.firstname", is(CUSTOMER.getFirstname())))
                .andExpect(jsonPath("$.customer.lastname", is(CUSTOMER.getLastname())))
                .andExpect(jsonPath("$.customer.email", is(CUSTOMER.getEmail())))
                .andExpect(jsonPath("$.customer.address.street", is(CUSTOMER.getAddress().getStreet())))
                .andExpect(jsonPath("$.customer.address.city", is(CUSTOMER.getAddress().getCity())))
                .andExpect(jsonPath("$.customer.address.zipCode", is(CUSTOMER.getAddress().getZipCode())))
                .andExpect(jsonPath("$.customer.address.country", is(CUSTOMER.getAddress().getCountry())))
                .andExpect(jsonPath("$.customer.creditCard.number", is(CUSTOMER.getCreditCard().getNumber())))
                .andExpect(jsonPath("$.customer.creditCard.balance", is(CUSTOMER.getCreditCard().getBalance())))
                .andExpect(jsonPath("$.customer.creditCard.currency", is(CUSTOMER.getCreditCard().getCurrency())))
                .andExpect(jsonPath("$.customer.points", is(CUSTOMER.getPoints().intValue())))
                .andExpect(jsonPath("$.customer.tierLevel", is(CUSTOMER.getTierLevel().toString())))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }

    @Test
    public void testLoginCustomerWhenDoesNotExistExpectFailure() throws Exception
    {
        String username = "FAKE USERNAME";
        String password = "FAKE PASSWORD";
        final LoginDto loginDto = new LoginDto(username, password);

        CustomerDto CUSTOMER_DTO = new CustomerDto(new Message("ACCESS DENIED!", Message.Status.FAILURE), null);

        Mockito.when(profileReaderServiceMock.login(username, password)).thenReturn(CUSTOMER_DTO);

        String RQ = TestUtils.convertObjectToJsonText(loginDto);

        logger.info("RQ = " + RQ);

        String RS = mockMvc.perform(post("/ProfileReader/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonText(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message.message", equalTo(CUSTOMER_DTO.getMessage().getMessage())))
                .andExpect(jsonPath("$.message.status", equalTo(CUSTOMER_DTO.getMessage().getStatus().toString())))
                .andExpect(jsonPath("$.customer", isEmptyOrNullString()))
                .andReturn().getResponse().getContentAsString();

        logger.info("RS = " + RS);
    }
}
