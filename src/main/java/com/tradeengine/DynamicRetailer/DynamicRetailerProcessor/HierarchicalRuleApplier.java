package com.tradeengine.DynamicRetailer.DynamicRetailerProcessor;

import com.tradeengine.DynamicRetailer.entities.RuleType;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tradeengine.DynamicRetailer.entities.RuleType.*;

@Component
public class HierarchicalRuleApplier {

    public Order applyBestRules(List<Discount> discountList, Order order) {
        Optional<Discount> bestGainedPointsDiscount = getBestGainedPointsDiscount(discountList);
        Optional<Discount> bestPriceDiscount = getBestPriceDiscount(discountList);
        Optional<Discount> bestDiscountWithUpgradeTierLevel = getBestDiscountWithUpgradeTierLevel(discountList);

        if (bestGainedPointsDiscount.isPresent()) {
            order.getDiscountList().add(bestGainedPointsDiscount.get());
            order.setGainedPoints(bestGainedPointsDiscount.get().getGainedPoints());
        }

        if (bestPriceDiscount.isPresent()) {
            order.getDiscountList().add(bestPriceDiscount.get());
            order.setPrice(bestPriceDiscount.get().getDiscountedPrice());
        }

        if (bestDiscountWithUpgradeTierLevel.isPresent()) {
            order.getDiscountList().add(bestDiscountWithUpgradeTierLevel.get());
        }
        return order;
    }

    private Optional<Discount> getBestGainedPointsDiscount(List<Discount> discounts) {
        Discount maxGainedPointsDiscount = discounts.stream()
                .filter(discount -> discount.getRuleType() == GAINED_POINTS)
                .max((o1, o2) -> o1.getGainedPoints() - o2.getGainedPoints()).get();

        return Optional.ofNullable(maxGainedPointsDiscount);
    }


    private Optional<Discount> getBestPriceDiscount(List<Discount> discounts) {
        Discount minPriceDiscount = discounts.stream()
                .filter(discount -> discount.getRuleType() == AWARDED_PERCENT_PRICE_DISCOUNT || discount.getRuleType() == AWARDED_PRICE_DISCOUNT)
                .min(Comparator.comparingDouble(discount -> discount.getDiscountedPrice().getPrice())).get();

        return Optional.ofNullable(minPriceDiscount);
    }


    private Optional<Discount> getBestDiscountWithUpgradeTierLevel(List<Discount> discounts) {
        List<Discount> discountsWithUpgradeStatus = discounts.stream()
                .filter(discount -> discount.getRuleType() == CUSTOMER_STATUS_CHANGED)
                .collect(Collectors.toList());

        Collections.sort(discountsWithUpgradeStatus, (o1, o2) -> o1.getUpgradedTierLevel().getWeight() - o2.getUpgradedTierLevel().getWeight());

        return discountsWithUpgradeStatus.isEmpty() ? Optional.empty() : Optional.of(discountsWithUpgradeStatus.get(0));
    }
}
