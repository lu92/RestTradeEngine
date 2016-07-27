package com.tradeengine.ProfileReader.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Builder;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address
{
    private String street;
    private String city;
    private String zipCode;
    private String country;
}
