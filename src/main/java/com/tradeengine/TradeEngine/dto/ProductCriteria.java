package com.tradeengine.TradeEngine.dto;

import java.util.List;

public class ProductCriteria
{
    private String category;
    private long minPrice;
    private long maxPrice;
    private List<Criteria> criteriaList;

    class Criteria
    {
        private String property;
        private String value;
        private String unitOfValue;
        private String valueType;
        private Direct direct;
    }

    enum Direct
    {
        HIGHER, LOWER, EQUAL
    }

}
