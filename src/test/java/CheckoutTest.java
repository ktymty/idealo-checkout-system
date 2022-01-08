import model.PricingRules;
import model.Sku;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import promotions.BuyItemWithFixedDiscount;
import promotions.BuyNItemsWithDiscount;
import promotions.LineItemPromotionStrategy;
import repository.SkuRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Checkout Test")
class CheckoutTest {

    private PricingRules pricingRules;

    @BeforeEach
    void setUp() {

        // Setup repository with SKU
        SkuRepository skuRepository = new SkuRepository();
        skuRepository.add(Sku.builder().id("A").markedPrice(BigDecimal.valueOf(40)).build());
        skuRepository.add(Sku.builder().id("B").markedPrice(BigDecimal.valueOf(50)).build());
        skuRepository.add(Sku.builder().id("C").markedPrice(BigDecimal.valueOf(25)).build());
        skuRepository.add(Sku.builder().id("D").markedPrice(BigDecimal.valueOf(20)).build());
        skuRepository.add(Sku.builder().id("E").markedPrice(BigDecimal.valueOf(100)).build());

        // Setup promotions for SKU's
        List<LineItemPromotionStrategy> promotionStrategies = new ArrayList<>();
        // new promotions can be added for SKU's with "Buy N items for X Price"
        promotionStrategies.add(BuyNItemsWithDiscount.builder().setOfPromoSkuId(new HashSet<>(List.of("A"))).minCountOfSkuToGetDiscount(3).minCountOfSkuWithDiscount(1).discountValue(0.5).build());
        promotionStrategies.add(BuyNItemsWithDiscount.builder().setOfPromoSkuId(new HashSet<>(List.of("B"))).minCountOfSkuToGetDiscount(2).minCountOfSkuWithDiscount(1).discountValue(0.4).build());

        // new promotions can be added for SKU's with "Fixed percentage discount"
        promotionStrategies.add(BuyItemWithFixedDiscount.builder().setOfPromoSkuId(new HashSet<>(List.of("E"))).discountPercentage(10).build());

        pricingRules = PricingRules.builder().skuRepository(skuRepository).promotionStrategies(promotionStrategies).build();
    }

    @AfterEach
    void tearDown() {
        pricingRules = null;
    }

    public BigDecimal calculatePrice(String goods) {
        Checkout checkout = new Checkout(pricingRules);
        for (int i = 0; i < goods.length(); i++) {
            checkout.scan(String.valueOf(goods.charAt(i)));
        }
        return checkout.getTotal();
    }

    @Test
    void totals() {
        assertEquals(0, calculatePrice("").intValue());
        assertEquals(40, calculatePrice("A").intValue());
        assertEquals(90, calculatePrice("AB").intValue());
        assertEquals(135, calculatePrice("CDBA").intValue());

        assertEquals(80, calculatePrice("AA").intValue());
        assertEquals(100, calculatePrice("AAA").intValue());
        assertEquals(140, calculatePrice("AAAA").intValue());
        assertEquals(180, calculatePrice("AAAAA").intValue());
        assertEquals(200, calculatePrice("AAAAAA").intValue());

        assertEquals(150, calculatePrice("AAAB").intValue());
        assertEquals(180, calculatePrice("AAABB").intValue());
        assertEquals(200, calculatePrice("AAABBD").intValue());
        assertEquals(200, calculatePrice("DABABA").intValue());
    }

    @Test
    void incremental() {

        Checkout checkout = new Checkout(pricingRules);

        assertEquals(0, checkout.getTotal().intValue());

        checkout.scan("A");
        assertEquals(40, checkout.getTotal().intValue());

        checkout.scan("B");
        assertEquals(90, checkout.getTotal().intValue());

        checkout.scan("A");
        assertEquals(130, checkout.getTotal().intValue());

        checkout.scan("A");
        assertEquals(150, checkout.getTotal().intValue());

        checkout.scan("B");
        assertEquals(180, checkout.getTotal().intValue());
    }

    @Test
    void should_apply_fixed_percentage_discount() {
        assertEquals(130, calculatePrice("EA").intValue());
        assertEquals(180, calculatePrice("EE").intValue());
    }
}