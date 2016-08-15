package com.tradeengine.TradeEngine.dto.productCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCriteria {
    private String category;
    private Long minPrice;
    private Long maxPrice;
    private List<Criteria> criteriaList;

    public List<Criteria> getCriteriaList() {
        if (criteriaList == null) {
            criteriaList = new ArrayList<>();
        }
        return criteriaList;
    }
}
