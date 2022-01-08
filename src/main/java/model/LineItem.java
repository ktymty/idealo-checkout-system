package model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import promotions.LineItemPromotionStrategy;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineItem {
    private Sku sku;
    private int count;
    private BigDecimal totalPrice;
    private List<Class<? extends LineItemPromotionStrategy>> promotions;
}
