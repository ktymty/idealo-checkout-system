package model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import promotions.PromotionStrategy;
import repository.SkuRepository;

import java.util.List;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PricingRules {
    SkuRepository skuRepository;
    List<PromotionStrategy> promotionStrategies;
}
