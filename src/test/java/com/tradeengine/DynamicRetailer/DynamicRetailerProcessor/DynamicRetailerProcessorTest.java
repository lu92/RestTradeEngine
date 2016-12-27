package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor;

import com.tradeengine.DynamicRetailer.dto.AcceptancePriceCriteria;
import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.common.entities.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.tradeengine.ProfileReader.entities.TierLevel.PREMIUM;
import static com.tradeengine.ProfileReader.entities.TierLevel.SILVER;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DynamicRetailerConfiguration.class)
public class DynamicRetailerProcessorTest {

    @Autowired
    private DynamicRetailerProcessor dynamicRetailerProcessor;

    Logger logger = Logger.getLogger(this.getClass().getName());


    @Test
    public void f() {
        // given
        CustomerInfo customerInfo = CustomerInfo.builder().tierLevel(PREMIUM).build();
        Order order = Order.builder()
                .price(Price.builder().amount(40000).tax(10000).price(50000).currency("PLN").build())
                .build();

        // when
        Order process = dynamicRetailerProcessor.process(customerInfo, getRules(), order);

        // then
        int a = 9;

    }

    @Before
    public void setup() {

    }

    private List<RuleDto> getRules() {
        List<RuleDto> rulesList = new ArrayList<>();

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
        RuleDto customer_status_changed_rule = RuleDto.customerStatusChangedRule(PREMIUM, new AcceptancePriceCriteria(50000.0, null));
        customer_status_changed_rule.setRuleId(6L);

        rulesList.add(gained_points_rule);
        rulesList.add(price_discount_rule);
        rulesList.add(price_discount_for_Premium_rule);
        rulesList.add(percent_price_discount_rule);
        rulesList.add(percent_price_discount_for_Silver_rule);
        rulesList.add(customer_status_changed_rule);
        return rulesList;
    }
}