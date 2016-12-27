package com.tradeengine.TradeEngineAdapter.services.downlines;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.LoginDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Customer;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.layers.CustomerSupportLayer;
import com.tradeengine.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;

@Component
public class ProfileReaderRestService implements CustomerSupportLayer {

    private RestTemplate restTemplate = new RestTemplate();

    private final int PORT = 8080;
    private final String HOST_ADDRESS = "localhost";
    private final String PROFILE_READER_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/ProfileReader";

    @Override
    public CustomerDto getCustomer(long customerId) {
        return restTemplate.getForObject(PROFILE_READER_BASE_URL + "/" + customerId, CustomerDto.class);
    }

    @Override
    public CustomerDtoList getCustomerList() {
        return restTemplate.getForObject(PROFILE_READER_BASE_URL, CustomerDtoList.class);
    }

    @Override
    public CustomerDto deleteCustomer(long customerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<CustomerDto> customerDtoResponseEntity =
                restTemplate.exchange(PROFILE_READER_BASE_URL + "/" + customerId, HttpMethod.DELETE, entity, CustomerDto.class);
        return customerDtoResponseEntity.getBody();
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        return restTemplate.postForObject(PROFILE_READER_BASE_URL, createCustomerDto, CustomerDto.class);
    }

    @Override
    public CustomerDto updateCustomer(CustomerInfo customer) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//        ResponseEntity<CustomerDto> customerDtoResponseEntity =
//                restTemplate.exchange(PROFILE_READER_BASE_URL, HttpMethod.PUT, entity, CustomerDto.class, customer);
//        return customerDtoResponseEntity.getBody();

//        final String uri = "http://localhost:8080/springrestexample/employees/{id}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "2");
        params.put("Content-Type", MediaType.APPLICATION_JSON.toString());
        params.put("Accept", MediaType.APPLICATION_JSON.toString());

//        EmployeeVO updatedEmployee = new EmployeeVO(2, "New Name", "Gilly", "test@email.com");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put (PROFILE_READER_BASE_URL, customer, params);
        return null;
    }

    @Override
    public CustomerDto login(String username, String password) {
        return restTemplate.postForObject(PROFILE_READER_BASE_URL + "/login", new LoginDto(username, password), CustomerDto.class);
    }
}
