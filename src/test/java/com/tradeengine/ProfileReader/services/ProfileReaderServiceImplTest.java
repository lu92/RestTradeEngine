package com.tradeengine.ProfileReader.services;

import com.tradeengine.MyMatchers;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.ProfileReaderIntegrationContext;
import com.tradeengine.ProfileReader.entities.TierLevel;
import com.tradeengine.ProfileReader.mapper.ProfileReaderMapper;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.common.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.tradeengine.ProfileReaderTestData.CUSTOMER;
import static com.tradeengine.ProfileReaderTestData.CUSTOMER_2;
import static com.tradeengine.ProfileReaderTestData.CUSTOMER_FIRSTNAME_2;
import static com.tradeengine.ProfileReaderTestData.*;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ProfileReaderIntegrationContext.class })
@Rollback(true)
public class ProfileReaderServiceImplTest
{
    @Autowired
    private ProfileReaderServiceImpl profileReaderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProfileReaderMapper profileReaderMapper;

    @Test
    @Transactional
    public void testCreateAndGetCustomer()
    {
        CustomerDto customerDto_CREATE = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto_CREATE.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto_CREATE.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_CREATE.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        CustomerDto customerDto_GET = profileReaderService.getCustomer(customerDto_CREATE.getCustomer().getCustomerId());
        assertThat(customerDto_GET.getMessage().getMessage()).isEqualTo("customer has been delivered!");
        assertThat(customerDto_GET.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        Assert.assertThat(profileReaderMapper.mapCustomer(customerDto_GET.getCustomer()), MyMatchers.sameAs(CUSTOMER));
    }

    @Test
    @Transactional
    public void testTryToCreateTwoSameCustomersExpectFailure()
    {
        CustomerDto customerDto_CREATE_1time = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto_CREATE_1time.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto_CREATE_1time.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_CREATE_1time.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        CustomerDto customerDto_CREATE_2time = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto_CREATE_2time.getMessage().getMessage()).isEqualTo("customer already exists!");
        assertThat(customerDto_CREATE_2time.getMessage().getStatus()).isEqualTo(Message.Status.FAILURE);
        assertThat(customerDto_CREATE_2time.getCustomer()).isNull();

        assertThat(customerRepository.count()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void testGetCustomerListExpectTwoCustomers()
    {
        CustomerDto customerDto_CREATE_1time = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto_CREATE_1time.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto_CREATE_1time.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_CREATE_1time.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        CustomerDto customerDto_CREATE_2time = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO_2);

        assertThat(customerDto_CREATE_2time.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto_CREATE_2time.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_CREATE_2time.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(2L);

        CustomerDtoList customerDtoList_GET = profileReaderService.getCustomerList();
        assertThat(customerDtoList_GET.getMessage().getMessage()).isEqualTo("Customer's list is filled");
        assertThat(customerDto_CREATE_2time.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        Assert.assertThat(profileReaderMapper.mapCustomer(customerDtoList_GET.getCustomerList().get(0)), MyMatchers.sameAs(CUSTOMER));
        Assert.assertThat(profileReaderMapper.mapCustomer(customerDtoList_GET.getCustomerList().get(1)), MyMatchers.sameAs(CUSTOMER_2));
    }

    @Test
    @Transactional
    public void testDeleteCustomerExpectSuccess()
    {
        CustomerDto customerDto = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        CustomerDto customerDto_DELETE = profileReaderService.deleteCustomer(customerDto.getCustomer().getCustomerId());
        assertThat(customerDto_DELETE.getMessage().getMessage()).isEqualTo("customer has been deleted!");
        assertThat(customerDto_DELETE.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_DELETE.getCustomer()).isNull();

        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @Test
    @Transactional
    public void testTryDeleteTwoTimesCustomerExpectFailure()
    {
        CustomerDto customerDto = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        CustomerDto customerDto_DELETE_1time = profileReaderService.deleteCustomer(customerDto.getCustomer().getCustomerId());
        assertThat(customerDto_DELETE_1time.getMessage().getMessage()).isEqualTo("customer has been deleted!");
        assertThat(customerDto_DELETE_1time.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_DELETE_1time.getCustomer()).isNull();

        assertThat(customerRepository.count()).isEqualTo(0L);

        CustomerDto customerDto_DELETE_2time = profileReaderService.deleteCustomer(customerDto.getCustomer().getCustomerId());
        assertThat(customerDto_DELETE_2time.getMessage().getMessage()).isEqualTo("customer doesn't exist!");
        assertThat(customerDto_DELETE_2time.getMessage().getStatus()).isEqualTo(Message.Status.FAILURE);
        assertThat(customerDto_DELETE_2time.getCustomer()).isNull();

        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @Test
    @Transactional
    public void testUpdateCustomerExpectSuccess() throws CloneNotSupportedException
    {
        CustomerDto customerDto = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        Customer CUSTOMER_COPY = (Customer) CUSTOMER.clone();

        CUSTOMER_COPY.setCustomerId(customerDto.getCustomer().getCustomerId());
        CUSTOMER_COPY.setFirstname(CUSTOMER_FIRSTNAME_2);
        CUSTOMER_COPY.setLastname(CUSTOMER_LASTNAME_2);
        CUSTOMER_COPY.setPoints(1000L);
        CUSTOMER_COPY.setTierLevel(TierLevel.GOLD);
        CustomerDto customerDto_UPDATE = profileReaderService.updateCustomer(profileReaderMapper.mapCustomer(CUSTOMER_COPY));

        assertThat(customerDto_UPDATE.getMessage().getMessage()).isEqualTo("customer has been updated!");
        assertThat(customerDto_UPDATE.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_UPDATE.getCustomer()).isNotNull();
        assertThat(customerDto_UPDATE.getCustomer().getFirstname()).isEqualTo(CUSTOMER_FIRSTNAME_2);
        assertThat(customerDto_UPDATE.getCustomer().getLastname()).isEqualTo(CUSTOMER_LASTNAME_2);
        assertThat(customerDto_UPDATE.getCustomer().getPoints()).isEqualTo(1000L);
        assertThat(customerDto_UPDATE.getCustomer().getTierLevel()).isEqualTo(TierLevel.GOLD);

        assertThat(customerRepository.count()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void testUpdateWhenCustomerDoesNotExistExpectFailure()
    {
        CUSTOMER.setCustomerId(1L);
        CustomerDto customerDto_UPDATE = profileReaderService.updateCustomer(profileReaderMapper.mapCustomer(CUSTOMER));

        assertThat(customerDto_UPDATE.getMessage().getMessage()).isEqualTo("can't update because of customer doesn't exist!");
        assertThat(customerDto_UPDATE.getMessage().getStatus()).isEqualTo(Message.Status.FAILURE);
        assertThat(customerDto_UPDATE.getCustomer()).isNull();

        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @Test
    @Transactional
    public void testUpdateWhenCustomerIdIsEmptyExpectFailure()
    {
        CUSTOMER.setCustomerId(null);
        CustomerDto customerDto_UPDATE = profileReaderService.updateCustomer(profileReaderMapper.mapCustomer(CUSTOMER));

        assertThat(customerDto_UPDATE.getMessage().getMessage()).isEqualTo("customerId is demanded!");
        assertThat(customerDto_UPDATE.getMessage().getStatus()).isEqualTo(Message.Status.FAILURE);
        assertThat(customerDto_UPDATE.getCustomer()).isNull();

        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @Test
    @Transactional
    public void loginWhenCustomerExistExpectSuccess()
    {
        CustomerDto customerDto_CREATE = profileReaderService.createCustomer(CREATE_CUSTOMER_DTO);

        assertThat(customerDto_CREATE.getMessage().getMessage()).isEqualTo("customer has been added!");
        assertThat(customerDto_CREATE.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_CREATE.getCustomer()).isNotNull();

        assertThat(customerRepository.count()).isEqualTo(1L);

        CustomerDto customerDto_LOGIN =
                profileReaderService.login(customerDto_CREATE.getCustomer().getUsername(), customerDto_CREATE.getCustomer().getPassword());

        assertThat(customerDto_LOGIN.getMessage().getMessage()).isEqualTo("ACCESS GRANTED!");
        assertThat(customerDto_LOGIN.getMessage().getStatus()).isEqualTo(Message.Status.SUCCESS);
        assertThat(customerDto_LOGIN.getCustomer().getFirstname()).isEqualTo(CREATE_CUSTOMER_DTO.getFirstname());
        assertThat(customerDto_LOGIN.getCustomer().getLastname()).isEqualTo(CREATE_CUSTOMER_DTO.getLastname());
    }

    @Test
    @Transactional
    public void loginWhenCustomerDoesNotExistExpectFailure()
    {
        CustomerDto customerDto_LOGIN =
                profileReaderService.login("FAKE_USERNAME", "FAKE_PASSWORD");

        assertThat(customerDto_LOGIN.getMessage().getMessage()).isEqualTo("ACCESS DENIED!");
        assertThat(customerDto_LOGIN.getMessage().getStatus()).isEqualTo(Message.Status.FAILURE);
        assertThat(customerDto_LOGIN.getCustomer()).isNull();
    }
}
