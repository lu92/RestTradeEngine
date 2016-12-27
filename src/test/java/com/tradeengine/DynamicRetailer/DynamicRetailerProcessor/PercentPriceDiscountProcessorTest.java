package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor;

import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.PercentPriceDiscountProcessor;
import com.tradeengine.DynamicRetailer.dto.AcceptancePriceCriteria;
import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.common.entities.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.tradeengine.DynamicRetailer.entities.RuleType.AWARDED_PERCENT_PRICE_DISCOUNT;
import static com.tradeengine.ProfileReader.entities.TierLevel.*;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PercentPriceDiscountProcessorTest {

    private PercentPriceDiscountProcessor percentPriceDiscountProcessor
            = new PercentPriceDiscountProcessor();

    @Test
    public void shouldApplyBasicRule() {

        // given
        RuleDto percent_price_discount_rule = RuleDto.percentPriceDiscountRule(1.0, new AcceptancePriceCriteria(10000.0, null));
        percent_price_discount_rule.setRuleId(4L);

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(9000).tax(1000).price(10000).currency("PLN").build())
                .build();
        // when
        assertThat(percentPriceDiscountProcessor.isApply(new CustomerInfo(), percent_price_discount_rule, order)).isTrue();
        Discount discount = percentPriceDiscountProcessor.calculate(percent_price_discount_rule, order);

        // then
        assertThat(discount.getRuleId()).isEqualTo(4L);
        assertThat(discount.getRuleType()).isEqualTo(AWARDED_PERCENT_PRICE_DISCOUNT);
        assertThat(discount.getGainedPoints()).isNull();
        assertThat(discount.getProductId()).isNull();
        assertThat(discount.getUpgradedTierLevel()).isNull();
        assertThat(discount.getOldPrice()).isEqualTo(order.getPrice());
        assertThat(discount.getDiscountedPrice()).isEqualTo(Price.builder().amount(8910).tax(990).price(9900).currency("PLN").build());
    }

    @Test
    public void shouldApplyTierLevelRule() {

        // given
        RuleDto percent_price_discount_for_Silver_rule = RuleDto.percentPriceDiscountRule(SILVER, 3.5, new AcceptancePriceCriteria(100000.0, null));
        percent_price_discount_for_Silver_rule.setRuleId(5L);

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(80000).tax(20000).price(100000).currency("PLN").build())
                .build();

        CustomerInfo customerInfo = CustomerInfo.builder()
                .tierLevel(SILVER)
                .build();
        // when
        assertThat(percentPriceDiscountProcessor.isApply(customerInfo, percent_price_discount_for_Silver_rule, order)).isTrue();
        Discount discount = percentPriceDiscountProcessor.calculate(percent_price_discount_for_Silver_rule, order);

        // then
        assertThat(discount.getRuleId()).isEqualTo(5L);
        assertThat(discount.getRuleType()).isEqualTo(AWARDED_PERCENT_PRICE_DISCOUNT);
        assertThat(discount.getGainedPoints()).isNull();
        assertThat(discount.getProductId()).isNull();
        assertThat(discount.getUpgradedTierLevel()).isNull();
        assertThat(discount.getOldPrice()).isEqualTo(order.getPrice());
        assertThat(discount.getDiscountedPrice()).isEqualTo(Price.builder().amount(77200).tax(19300).price(96500).currency("PLN").build());
    }

    @Test
    public void rejectByTierLevel() {
        // given
        RuleDto price_discount_for_Premium_rule = RuleDto.priceDiscountRule(PREMIUM, 1000.0, new AcceptancePriceCriteria(50000.0, null));
        price_discount_for_Premium_rule.setRuleId(3L);

        CustomerInfo customerInfo = CustomerInfo.builder()
                .tierLevel(STANDARD)
                .build();

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(5000).tax(300).price(5300).build())
                .build();
        // when
        assertThat(percentPriceDiscountProcessor.isApply(customerInfo, price_discount_for_Premium_rule, order)).isFalse();
    }

    @Test
    public void rejectByTooLowPrice() {
        // given
        RuleDto price_discount_rule = RuleDto.priceDiscountRule(200.0, new AcceptancePriceCriteria(6000.0, null));
        price_discount_rule.setRuleId(2L);

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(5000).tax(300).price(5300).build())
                .build();
        // when
        assertThat(percentPriceDiscountProcessor.isApply(new CustomerInfo(), price_discount_rule, order)).isFalse();
    }

    @Test
    public void rejectByToHighPrice() {

        // given
        RuleDto price_discount_rule = RuleDto.priceDiscountRule(200.0, new AcceptancePriceCriteria(null, 5000.0));
        price_discount_rule.setRuleId(2L);

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(5000).tax(300).price(5300).build())
                .build();
        // when
        assertThat(percentPriceDiscountProcessor.isApply(new CustomerInfo(), price_discount_rule, order)).isFalse();
    }
}