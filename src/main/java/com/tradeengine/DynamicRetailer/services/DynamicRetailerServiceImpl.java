package com.tradeengine.DynamicRetailer.services;

import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.DynamicRetailerProcessor;
import com.tradeengine.DynamicRetailer.dto.AcceptancePriceCriteria;
import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.DynamicRetailer.dto.RuleListDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.TradeEngineAdapter.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.tradeengine.ProfileReader.entities.TierLevel.PREMIUM;
import static com.tradeengine.ProfileReader.entities.TierLevel.SILVER;

@Service
public class DynamicRetailerServiceImpl implements DynamicRetailerService {

    @Autowired
    private DynamicRetailerProcessor dynamicRetailerProcessor;

    private final int PORT = 8080;
    private final String HOST_ADDRESS = "localhost";
    private final String PROFILE_READER_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/ProfileReader";

    private RestTemplate restTemplate = new RestTemplate();


    @Override
    public RuleListDto getRules() {
        return new RuleListDto(getSampleRules());
    }

    @Override
    public Order calculateDiscount(Order order) {
        CustomerDto customerDto = restTemplate.getForObject(PROFILE_READER_BASE_URL + "/" + order.getCustomerId(), CustomerDto.class);

        return dynamicRetailerProcessor.process(customerDto.getCustomer(), getSampleRules(), order);
    }

    public List<RuleDto> getSampleRules() {
        // gained-points rule
        RuleDto gained_points_rule = RuleDto.gainedPointsRule(100, new AcceptancePriceCriteria(25000.0, null));
        gained_points_rule.setRuleId(1L);

        // price-discount rule
        RuleDto price_discount_rule = RuleDto.priceDiscountRule(200.0, new AcceptancePriceCriteria(null, 5000.0));
        price_discount_rule.setRuleId(2L);

        // price-discount rule
        RuleDto price_discount_for_Premium_rule = RuleDto.priceDiscountRule(PREMIUM, 1000.0, new AcceptancePriceCriteria(50000.0, null));
        price_discount_for_Premium_rule.setRuleId(3L);

        // percent-price-discount rule
        RuleDto percent_price_discount_rule = RuleDto.percentPriceDiscountRule(1.0, new AcceptancePriceCriteria(10000.0, null));
        percent_price_discount_rule.setRuleId(4L);

        // percent-price-discount rule
        RuleDto percent_price_discount_for_Silver_rule = RuleDto.percentPriceDiscountRule(SILVER, 3.5, new AcceptancePriceCriteria(100000.0, null));
        percent_price_discount_for_Silver_rule.setRuleId(5L);

        // customer-status-changed rule
        RuleDto customer_status_changed_rule = RuleDto.customerStatusChangedRule(PREMIUM, new AcceptancePriceCriteria(20000.0, null));
        customer_status_changed_rule.setRuleId(6L);

        return Arrays.asList(gained_points_rule, price_discount_rule, price_discount_for_Premium_rule,
                percent_price_discount_rule, percent_price_discount_for_Silver_rule, customer_status_changed_rule);
    }
}
