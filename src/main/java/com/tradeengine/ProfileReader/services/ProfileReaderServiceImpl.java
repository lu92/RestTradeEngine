package com.tradeengine.ProfileReader.services;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.ProfileReader.entities.TierLevel;
import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.common.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProfileReaderServiceImpl implements ProfileReaderService
{
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto getCustomer(long customerId)
    {
        if (customerRepository.exists(customerId))
        {
            return new CustomerDto(new Message("customer has been delivered!", Message.Status.SUCCESS), customerRepository.getOne(customerId));
        }
        else
        {
            return new CustomerDto(new Message("customer doesn't exist!", Message.Status.FAILURE), null);
        }
    }

    @Override
    public CustomerDtoList getCustomerList()
    {
        List<Customer> customerList = customerRepository.findAll();
        if (customerList.isEmpty())
        {
            return new CustomerDtoList(new Message("Customer's list is empty", Message.Status.SUCCESS), customerList);
        }
        else
        {
            return new CustomerDtoList(new Message("Customer's list is filled", Message.Status.SUCCESS), customerList);
        }
    }

    @Override
    public CustomerDto deleteCustomer(long customerId)
    {
        if (customerRepository.exists(customerId))
        {
            customerRepository.delete(customerId);
            return new CustomerDto(new Message("customer has been deleted!", Message.Status.SUCCESS), null);
        }
        else
        {
            return new CustomerDto(new Message("customer doesn't exist!", Message.Status.FAILURE), null);
        }
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto customerDto)
    {
        if (customerExists(customerDto))
        {
            return new CustomerDto(new Message("customer already exists!", Message.Status.FAILURE), null);
        }
        else
        {
            Customer customer = Customer.builder()
                    .firstname(customerDto.getFirstname())
                    .lastname(customerDto.getLastname())
                    .email(customerDto.getEmail())
                    .birthday(customerDto.getBirthday())
                    .address(customerDto.getAddress())
                    .creditCard(customerDto.getCreditCard())
                    .username(customerDto.getUsername())
                    .password(customerDto.getPassword())
                    .creationDate(LocalDate.now())
                    .points(0L)
                    .tierLevel(TierLevel.STANDARD)
                    .build();

            Customer customerDb = customerRepository.save(customer);
            return new CustomerDto(new Message("customer has been added!", Message.Status.SUCCESS), customerDb);
        }
    }

    @Override
    public CustomerDto updateCustomer(Customer customer)
    {
        if (customer.getCustomerId() == null)
        {
            return new CustomerDto(new Message("customerId is demanded!", Message.Status.FAILURE), null);
        }
        else
        {
            if (customer.getCustomerId() != null && customerRepository.exists(customer.getCustomerId()))
            {
                return new CustomerDto(new Message("customer has been updated!", Message.Status.SUCCESS), customerRepository.save(customer));
            }
            else
            {
                return new CustomerDto(new Message("can't update because of customer doesn't exist!", Message.Status.FAILURE), null);
            }
        }
    }

    @Override
    public CustomerDto login(String username, String password)
    {
        List<Customer> findedByUsernameAndPassword = customerRepository.findByUsernameAndPassword(username, password);
        if (findedByUsernameAndPassword.size() == 1)
        {
            Customer customer = findedByUsernameAndPassword.get(0);
            return new CustomerDto(new Message("ACCESS GRANTED!", Message.Status.SUCCESS), customer);
        }
        else
        {
            // when didn't find proper customer
            return new CustomerDto(new Message("ACCESS DENIED!", Message.Status.FAILURE), null);
        }
    }

    private boolean customerExists(CreateCustomerDto customer)
    {
        for (Customer customerDb : customerRepository.findAll())
        {
            if (sameCustomers(customer, customerDb))
                return true;
        }
        return false;
    }

    private boolean sameCustomers(CreateCustomerDto customer1, Customer customer2)
    {
        if (StringUtils.equals(customer1.getFirstname(), customer2.getFirstname()) &&
                StringUtils.equals(customer1.getLastname(), customer2.getLastname()) &&
                customer1.getBirthday().equals(customer2.getBirthday()) &&
                StringUtils.equals(customer1.getAddress().getStreet(), customer2.getAddress().getStreet()) &&
                StringUtils.equals(customer1.getAddress().getCity(), customer2.getAddress().getCity()) &&
                StringUtils.equals(customer1.getAddress().getZipCode(), customer2.getAddress().getZipCode()) &&
                StringUtils.equals(customer1.getAddress().getCountry(), customer2.getAddress().getCountry()) &&
                StringUtils.equals(customer1.getCreditCard().getNumber(), customer2.getCreditCard().getNumber()))
            return true;
        else
            return false;
    }
}
