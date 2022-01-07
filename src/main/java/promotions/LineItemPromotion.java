package promotions;

import model.LineItem;
import model.ShoppingCart;

public interface LineItemPromotion extends PromotionStrategy {
    void applyDiscount(LineItem lineItem);

    default boolean filter(LineItem lineItem) {
        return true;
    }

    default void apply(ShoppingCart shoppingCart) {
        shoppingCart.getLineItems().stream()
                .filter(this::filter)
                .forEach(this::applyDiscount);
    }
}
