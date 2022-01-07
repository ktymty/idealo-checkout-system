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
    private HashSet<String> setOfPromoSkuId;
    private double discountPercentage; // means discount in %age i.e 10 means 10%

    @Override
    public void applyDiscount(LineItem lineItem) {
        // discount cannot be more than 100%, hence fallback
        if (discountPercentage > 100) {
            discountPercentage = 100;
        }
        // discount cannot be less than 0%, hence fallback
        if (discountPercentage < 0) {
            discountPercentage = 0;
        }

        double markedPricePercentage = 100 - discountPercentage;

        BigDecimal skuPriceAfterDiscount = lineItem.getSku().getMarkedPrice().multiply(BigDecimal.valueOf(markedPricePercentage)).divide(BigDecimal.valueOf(100));
        lineItem.setTotalPrice(skuPriceAfterDiscount.multiply(BigDecimal.valueOf(lineItem.getCount())));
    }

    @Override
    public boolean filter(LineItem lineItem) {
        return setOfPromoSkuId.contains(lineItem.getSku().getId());
    }
}
