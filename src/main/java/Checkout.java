import model.LineItem;
import model.PricingRules;
import model.ShoppingCart;

import java.math.BigDecimal;

public class Checkout {
    private final PricingRules pricingRules;
    private final ShoppingCart shoppingCart;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public Checkout(PricingRules pricingRules) {
        this.pricingRules = pricingRules;
        shoppingCart = ShoppingCart.builder().skuRepository(pricingRules.getSkuRepository()).build();
    }

    public BigDecimal getTotal() {
        return totalPrice;
    }

    public void scan(String skuId) {
        shoppingCart.addSku(skuId);
        pricingRules.getPromotionStrategies().forEach(promo -> promo.apply(shoppingCart));
        totalPrice = shoppingCart.getLineItems().stream()
                .map(LineItem::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}
