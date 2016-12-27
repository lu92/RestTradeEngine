package com.tradeengine.ShoppingHistory.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompletedOrderDto {
    private long customerId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeOfSale;
    private List<SoldProductInfo> soldProductsList;
    private Address address;
    private Integer gainedPoints;
    private Price cost;

    public List<SoldProductInfo> getSoldProductsList() {
        if (soldProductsList == null) {
            soldProductsList = new ArrayList<>();
        }
        return soldProductsList;
    }
}
