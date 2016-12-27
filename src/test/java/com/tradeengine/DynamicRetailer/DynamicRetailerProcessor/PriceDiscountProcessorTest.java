package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor;

import com.tradeengine.DynamicRetailer.DynamicRetailerProcessor.ruleProcessors.PriceDiscountProcessor;
import com.tradeengine.DynamicRetailer.dto.AcceptancePriceCriteria;
import com.tradeengine.DynamicRetailer.dto.RuleDto;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.common.entities.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.tradeengine.DynamicRetailer.entities.RuleType.AWARDED_PRICE_DISCOUNT;
import static com.tradeengine.ProfileReader.entities.TierLevel.PREMIUM;
import static com.tradeengine.ProfileReader.entities.TierLevel.STANDARD;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PriceDiscountProcessorTest {

    private PriceDiscountProcessor priceDiscountProcessor
            = new PriceDiscountProcessor();

    @Test
    public void shouldApplyBasicRule() {

        // given
        RuleDto price_discount_rule = RuleDto.priceDiscountRule(200.0, new AcceptancePriceCriteria(null, 5000.0));
        price_discount_rule.setRuleId(2L);

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(3000).tax(300).price(3300).currency("PLN").build())
                .build();
        // when
        assertThat(priceDiscountProcessor.isApply(new CustomerInfo(), price_discount_rule, order)).isTrue();
        Discount discount = priceDiscountProcessor.calculate(price_discount_rule, order);

        // then
        assertThat(discount.getRuleId()).isEqualTo(2L);
        assertThat(discount.getRuleType()).isEqualTo(AWARDED_PRICE_DISCOUNT);
        assertThat(discount.getGainedPoints()).isNull();
        assertThat(discount.getProductId()).isNull();
        assertThat(discount.getUpgradedTierLevel()).isNull();
        assertThat(discount.getOldPrice()).isEqualTo(order.getPrice());
        assertThat(discount.getDiscountedPrice()).isEqualTo(Price.builder().amount(2800).tax(300).price(3100).currency("PLN").build());
    }

    @Test
    public void shouldApplyTierLevelRule() {

        // given
        RuleDto price_discount_for_Premium_rule = RuleDto.priceDiscountRule(PREMIUM, 1000.0, new AcceptancePriceCriteria(50000.0, null));
        price_discount_for_Premium_rule.setRuleId(3L);

        Order order = Order.builder()
                .customerId(1L)
                .price(Price.builder().amount(60000).tax(13000).price(73000).currency("PLN").build())
                .build();

        CustomerInfo customerInfo = CustomerInfo.builder()
                .tierLevel(PREMIUM)
                .build();
        // when
        assertThat(priceDiscountProcessor.isApply(customerInfo, price_discount_for_Premium_rule, order)).isTrue();
        Discount discount = priceDiscountProcessor.calculate(price_discount_for_Premium_rule, order);

        // then
        assertThat(discount.getRuleId()).isEqualTo(3L);
        assertThat(discount.getRuleType()).isEqualTo(AWARDED_PRICE_DISCOUNT);
        assertThat(discount.getGainedPoints()).isNull();
        assertThat(discount.getProductId()).isNull();
        assertThat(discount.getUpgradedTierLevel()).isNull();
        assertThat(discount.getOldPrice()).isEqualTo(order.getPrice());
        assertThat(discount.getDiscountedPrice()).isEqualTo(Price.builder().amount(59000).tax(13000).price(72000).currency("PLN").build());
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
        assertThat(priceDiscountProcessor.isApply(new CustomerInfo(), price_discount_for_Premium_rule, order)).isFalse();
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
        assertThat(priceDiscountProcessor.isApply(new CustomerInfo(), price_discount_rule, order)).isFalse();
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
        assertThat(priceDiscountProcessor.isApply(new CustomerInfo(), price_discount_rule, order)).isFalse();
    }
}