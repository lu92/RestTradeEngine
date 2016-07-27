package com.tradeengine.TradeEngineAdapter.services.processors;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ShoppingHistory.ShoppingHistoryDto;
import com.tradeengine.TradeEngineAdapter.model.Customer;
import com.tradeengine.TradeEngineAdapter.model.ShoppingHistory;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.layers.CustomerSupportLayer;
import com.tradeengine.common.Message;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;

@Component
public class CustomerProcessor implements CustomerSupportLayer
{
    private RestTemplate restTemplate = new RestTemplate();

    private final int PORT = 8080;
    private final String HOST_ADDRESS = "localhost";

    private final String PROFILE_READER_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/ProfileReader";
    private final String SHOPPING_HISTORY_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/ShoppingHistory";

    @Override
    public CustomerDto getCustomer(long customerId)
    {
        return restTemplate.getForObject(PROFILE_READER_BASE_URL + "/" + customerId, CustomerDto.class);
    }

    @Override
    public CustomerDtoList getCustomerList()
    {
        return restTemplate.getForObject(PROFILE_READER_BASE_URL, CustomerDtoList.class);
    }

    @Override
    public CustomerDto deleteCustomer(long customerId)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<CustomerDto> customerDtoResponseEntity =
                restTemplate.exchange(PROFILE_READER_BASE_URL + "/" + customerId, HttpMethod.DELETE, entity, CustomerDto.class);
        return customerDtoResponseEntity.getBody();
    }

//    @Override
//    public CustomerDTO createCustomer(com.tradeengine.ProfileReader.entities.Customer customer)
//    {
//        CustomerDTO customerDto = restTemplate.postForObject(PROFILE_READER_BASE_URL, customer, CustomerDTO.class);
//        //        if (customerDto.getMessage().getStatus() == SUCCESS)
//        //        {
//        ShoppingHistoryDto shoppingHistoryDto = restTemplate.postForObject(SHOPPING_HISTORY_BASE_URL, customerDto.getCustomer().getCustomerId(), ShoppingHistoryDto.class);
//        //            if (shoppingHistoryDto.getMessage().getStatus() == SUCCESS)
//        //            {
//        return customerDto;
//        //            }
//        //        }
//
//    }

    @Override public CustomerDto createCustomer(CreateCustomerDto createCustomerDto)
    {
        CustomerDto customerDto = restTemplate.postForObject(PROFILE_READER_BASE_URL, createCustomerDto, CustomerDto.class);
        //        if (customerDto.getMessage().getStatus() == SUCCESS)
        //        {
        ShoppingHistoryDto shoppingHistoryDto = restTemplate.postForObject(SHOPPING_HISTORY_BASE_URL, customerDto.getCustomer().getCustomerId(), ShoppingHistoryDto.class);
        //            if (shoppingHistoryDto.getMessage().getStatus() == SUCCESS)
        //            {
        return customerDto;
    }

    @Override
    public CustomerDto updateCustomer(com.tradeengine.ProfileReader.entities.Customer customer)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<CustomerDto> customerDtoResponseEntity =
                restTemplate.exchange(PROFILE_READER_BASE_URL, HttpMethod.PUT, entity, CustomerDto.class, customer);
        return customerDtoResponseEntity.getBody();
    }

    @Override public CustomerDto login(String username, String password)
    {
        return null;
    }

    @Override
    public CustomerDTO getCustomerWithShoppingHistory(long customerId)
    {
        final CustomerDto PR_CUSTOMER = restTemplate.getForObject(PROFILE_READER_BASE_URL + "/" + customerId, CustomerDto.class);

        if (PR_CUSTOMER.getMessage().getStatus() == FAILURE)
        {
            return new CustomerDTO(PR_CUSTOMER.getMessage(), null);
        }
        else
        {
            final ShoppingHistoryDto SH_SHOPPING_HISTORY = restTemplate.getForObject(SHOPPING_HISTORY_BASE_URL + "/" + customerId, ShoppingHistoryDto.class);
            if (SH_SHOPPING_HISTORY.getMessage().getStatus() == FAILURE)
            {
                return new CustomerDTO(SH_SHOPPING_HISTORY.getMessage(), null);
            }
            else
            {
                return createFullInfoAboutCustomer(PR_CUSTOMER, SH_SHOPPING_HISTORY);
            }
        }
    }

    private CustomerDTO createFullInfoAboutCustomer(CustomerDto pr_customer, ShoppingHistoryDto sh_shopping_history)
    {
        // sprawdzic czy customer ma historie

        CustomerShoppingHistoryInfoBuilder customerShoppingHistoryInfoBuilder = new CustomerShoppingHistoryInfoBuilder();
        Customer customer = customerShoppingHistoryInfoBuilder.customer(pr_customer).shoppingHistory(sh_shopping_history).build();
        return new CustomerDTO(new Message("Customer with his shopping history has been delivered!", SUCCESS), customer);
    }

}
