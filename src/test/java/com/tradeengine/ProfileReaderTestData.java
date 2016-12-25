package com.tradeengine;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.ProfileReader.entities.CreditCard;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.ProfileReader.entities.TierLevel;
import com.tradeengine.common.Message;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class ProfileReaderTestData
{
    public static final String CITY = "London";
    public static final String COUNTRY = "England";
    public static final String STREET = "Abbey Road 1";
    public static final String ZIP_CODE = "20-100 London";

    public static final String CUSTOMER_FIRSTNAME = "James";
    public static final String CUSTOMER_LASTNAME = "Bond";

    public static final String CUSTOMER_FIRSTNAME_2 = "Jan";
    public static final String CUSTOMER_LASTNAME_2 = "Kowalski";

    public static final LocalDate CUSTOMER_BIRTHDAY = LocalDate.of(1980, Month.JANUARY, 1);

    public static final String EMAIL = CUSTOMER_FIRSTNAME + "." + CUSTOMER_LASTNAME + "@gmail.com";
    public static final LocalDate CUSTOMER_BIRTHDAY_2 = LocalDate.of(1970, Month.JANUARY, 1);
    public static final String EMAIL_2 = CUSTOMER_FIRSTNAME_2 + "." + CUSTOMER_LASTNAME_2 + "@gmail.com";

    public static final String CREDIT_CARD_NUMBER = "0000024534536456345345";
    public static final String CREDIT_CARD_NUMBER_2 = "1232224534536456345345";
    public static final String CURRENCY = "USD";
    public static final double BALANCE = 100000;

    public static final Address ADDRESS = Address.builder()
            .city(CITY).country(COUNTRY).street(STREET).zipCode(ZIP_CODE)
            .build();

    public static final CreditCard CREDIT_CARD = CreditCard.builder()
            .number(CREDIT_CARD_NUMBER)
            .balance(BALANCE)
            .currency(CURRENCY)
            .build();

    public static final CreditCard CREDIT_CARD_2 = CreditCard.builder()
            .number(CREDIT_CARD_NUMBER_2)
            .balance(BALANCE)
            .currency(CURRENCY)
            .build();

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    public static final Customer CUSTOMER = Customer.builder()
            .firstname(CUSTOMER_FIRSTNAME)
            .lastname(CUSTOMER_LASTNAME)
            .email(EMAIL)
            .birthday(CUSTOMER_BIRTHDAY)
            .address(ADDRESS)
            .creditCard(CREDIT_CARD)
            .username(USERNAME)
            .password(PASSWORD)
            .creationDate(LocalDate.now())
            .points(0L)
            .tierLevel(TierLevel.STANDARD)
            .build();

    public static final Customer CUSTOMER_2 = Customer.builder()
            .firstname(CUSTOMER_FIRSTNAME_2)
            .lastname(CUSTOMER_LASTNAME_2)
            .birthday(CUSTOMER_BIRTHDAY_2)
            .email(EMAIL_2)
            .address(ADDRESS)
            .creditCard(CREDIT_CARD_2)
            .creationDate(LocalDate.now())
            .points(0L)
            .tierLevel(TierLevel.STANDARD)
            .build();

    public static final CreateCustomerDto CREATE_CUSTOMER_DTO
            = new CreateCustomerDto(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, EMAIL,
            CUSTOMER_BIRTHDAY, ADDRESS, CREDIT_CARD, USERNAME, PASSWORD);

    public static final CreateCustomerDto CREATE_CUSTOMER_DTO_2
            = new CreateCustomerDto(CUSTOMER_FIRSTNAME_2, CUSTOMER_LASTNAME_2, EMAIL_2,
            CUSTOMER_BIRTHDAY_2, ADDRESS, CREDIT_CARD_2, USERNAME, PASSWORD);
}
