package promotions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import model.LineItem;

import java.math.BigDecimal;
import java.util.HashSet;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuyItemWithFixedDiscount implements LineItemPromotion {

    private static final double MAX_DISCOUNT = 100;
    private static final double MIN_DISCOUNT = 0;

    private HashSet<String> setOfPromoSkuId;
    private double discountPercentage; // means discount in %age i.e 10 means 10%

    @Override
    public void applyDiscount(LineItem lineItem) {
        // discount cannot be more than 100%, hence fallback
        if (discountPercentage > MAX_DISCOUNT) {
            discountPercentage = MAX_DISCOUNT;
        }
        // discount cannot be less than 0%, hence fallback
        if (discountPercentage < MIN_DISCOUNT) {
            discountPercentage = MIN_DISCOUNT;
        }

        double markedPricePercentage = 100 - discountPercentage;

        BigDecimal skuPriceAfterDiscount = lineItem.getSku().getMarkedPrice().
                multiply(BigDecimal.valueOf(markedPricePercentage)).
                divide(BigDecimal.valueOf(100));

        lineItem.setTotalPrice(skuPriceAfterDiscount.multiply(BigDecimal.valueOf(lineItem.getCount())));
    }

    @Override
    public boolean filter(LineItem lineItem) {
        return setOfPromoSkuId.contains(lineItem.getSku().getId());
    }
}
