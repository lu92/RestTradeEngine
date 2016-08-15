package com.tradeengine.ShoppingHistory.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tradeengine.ShoppingHistory.entities.SoldProduct;
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
public class CompletedOrderInfo {
    private Long completedOrderId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeOfSale;

    private List<SoldProduct> soldProductsList;

    private Long ShoppingHistoryId;

    private long gainedPoints;

    private Price price;

    public List<SoldProduct> getSoldProductsList() {
        if (soldProductsList == null) {
            soldProductsList = new ArrayList<>();
        }
        return soldProductsList;
    }
}
