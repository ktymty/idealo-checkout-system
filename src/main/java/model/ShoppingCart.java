package model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import repository.SkuRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCart {

    SkuRepository skuRepository;
    List<LineItem> lineItems = new ArrayList<>();

    public void addSku(String skuId) {
        Sku sku = skuRepository.findById(skuId);

        Optional<LineItem> lineItem = lineItems.stream()
                .filter(item -> skuId.equals(item.getSku().getId()))
                .findFirst();

        if (lineItem.isPresent()) {
            lineItem.get().setCount(lineItem.get().getCount() + 1);
        } else {
            int count = 1;
            lineItems.add(LineItem.builder().sku(sku).count(count).totalPrice(sku.getMarkedPrice().multiply(BigDecimal.valueOf(count))).build());
        }
    }
}
