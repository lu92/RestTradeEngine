package com.tradeengine.TradeEngineAdapter.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.ProfileReader.entities.CreditCard;
import com.tradeengine.ProfileReader.entities.TierLevel;
import com.tradeengine.TradeEngine.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class Customer
{
    private Long customerId;
    private String firstname;
    private String lastname;
    private String email;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate birthday;

    private Address address;
    private CreditCard creditCard;
    private String username;
    private String password;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate creationDate;

    private Long points;
    private TierLevel tierLevel;
    private ShoppingHistory shoppingHistory;
}
