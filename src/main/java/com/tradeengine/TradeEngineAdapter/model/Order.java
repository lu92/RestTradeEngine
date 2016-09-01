package com.tradeengine.TradeEngineAdapter.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.TradeEngine.dto.ProductInfo;
import com.tradeengine.common.Message;
import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long customerId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeOfSale;

    private List<ProductInfo> productList;
    private Address address;
    private Long gainedPoints;
    private List<Discount> discountList;
    private List<Error> flowResults;
    private Price price;
    private Message.Status status;

    public List<ProductInfo> getProductList() {
        if (productList == null)
            productList = new ArrayList<>();

        return productList;
    }

    public List<Discount> getDiscountList() {
        if (discountList == null)
            discountList = new ArrayList<>();

        return discountList;
    }

    public List<Error> getFlowResults() {
        if (flowResults == null) {
            flowResults = new ArrayList<>();
        }

        return flowResults;
    }

    public void addError(Error... errors) {
        getFlowResults().addAll(Arrays.asList(errors));
    }
}
