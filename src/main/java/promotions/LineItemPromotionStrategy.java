package promotions;

import model.LineItem;
import model.ShoppingCart;

/**
 * Interface all promotion algorithms must implement
 */
public interface LineItemPromotionStrategy {
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
