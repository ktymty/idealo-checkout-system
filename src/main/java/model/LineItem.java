package model;

import lombok.Data;
import promotions.PromotionStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class LineItem {
    Sku sku;
    int count;
    BigDecimal totalPrice;
    List<Class<? extends PromotionStrategy>> promotions = new ArrayList<>();

    public LineItem(Sku sku, int count) {
        this.sku = sku;
        this.count = count;
        totalPrice = sku.getMarkedPrice().multiply(BigDecimal.valueOf(count));
    }

}
