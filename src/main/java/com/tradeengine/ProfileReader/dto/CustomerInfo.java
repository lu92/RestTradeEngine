package com.tradeengine.ProfileReader.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.ProfileReader.entities.CreditCard;
import com.tradeengine.ProfileReader.entities.TierLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {
    private Long customerId;

    private String firstname;
    private String lastname;
    private String email;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    private Address address;
    private CreditCard creditCard;
    private String username;
    private String password;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate creationDate;

    private Long points;
    private TierLevel tierLevel;
}
