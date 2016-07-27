package com.tradeengine.ProfileReader.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable, Cloneable
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstname;
    private String lastname;
    private String email;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;

    private String username;
    private String password;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate creationDate;

    private Long points;
    private TierLevel tierLevel;

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new Customer(customerId, firstname, lastname, email, birthday, address, creditCard, username, password, creationDate, points, TierLevel.STANDARD);
    }
}
