package promotions;

import model.ShoppingCart;

public interface PromotionStrategy {
    void apply(ShoppingCart shoppingCart);
}
