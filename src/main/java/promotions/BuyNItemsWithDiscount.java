package promotions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import model.LineItem;

import java.math.BigDecimal;
import java.util.HashSet;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuyNItemsWithDiscount implements LineItemPromotion {

    HashSet<String> setOfPromoSkuId;
    int minCountOfSkuToGetDiscount;
    int minCountOfSkuWithDiscount;
    double discountValue;

    @Override
    public void applyDiscount(LineItem lineItem) {
        BigDecimal skuPrice = lineItem.getSku().getMarkedPrice();

        int totalSkuWithDiscountPerItem = (lineItem.getCount() / minCountOfSkuToGetDiscount) * minCountOfSkuWithDiscount;

        int totalSkuNoDiscountPerItem = lineItem.getCount() - totalSkuWithDiscountPerItem;

        lineItem.setTotalPrice(
                skuPrice.multiply(BigDecimal.valueOf(totalSkuNoDiscountPerItem))
                        .add(skuPrice.multiply(BigDecimal.valueOf(totalSkuWithDiscountPerItem * (1 - discountValue))))
        );
    }

    @Override
    public boolean filter(LineItem lineItem) {
        return setOfPromoSkuId.contains(lineItem.getSku().getId());
    }
}
