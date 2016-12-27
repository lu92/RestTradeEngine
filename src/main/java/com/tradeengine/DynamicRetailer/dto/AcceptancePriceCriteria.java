package com.tradeengine.DynamicRetailer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptancePriceCriteria {
    private Double startPrice;
    private Double endPrice;

    public boolean isStartPriceEmpty() {
        return startPrice == null;
    }

    public boolean isEndPriceEmpty() {
        return endPrice == null;
    }

    public void validate() throws IllegalArgumentException {
        if (isStartPriceEmpty() && isEndPriceEmpty())
            throw new IllegalArgumentException("Acceptance price critera is invalid!");
    }
}
