# Supermarket Checkout System
Implement the code for a supermarket’s checkout system with a customizable pricing schema.

[More details](Checkout_Coding_Challenge_EN.pdf)

## Steps

### 1. Download dependencies and build
```bash
mvn clean
```
### 2. Run unit tests
```bash
mvn test
```

## Tech Stack

The service is built using only Java (JDK 11) and Maven. Following dependencies have been used:
* [Lombok](https://projectlombok.org/) - to reduce boilerplate code.
* [Junit 5](https://junit.org/junit5/) - for unit tests.

Unit tests have been written with good code coverage.

## Tech Notes

1. The code is organized into following main packages:
    - **model**: This package represents the data model.
    - **promotions**: This package contains class and interface to apply promotions.
    - **repository**: This package contains class to store SKU's in-memory.
2. A Strategy Design pattern has been used to select a promotion during runtime. LineItemPromotionStrategy is the interface which each promotion algorithm must apply. This pattern supports SOLID pattern. The D in SOLID says we must depend on abstractions, not on concretions. Also, the O which says entities should be open for, not extensions. The I, we have one specific interface for the concrete strategy to implement. The S, all the classes implementing the strategy have only one job of applying discounts. The L, all subclasses of the concrete strategies are substitutable for their superclasses. Subclassing could have been an alternative approach, but we will eventually run into a code that is hard to understand and maintain as we will have many related classes with difference being on the algorithms they carry.

##  Exception

**NoSuchElementException**
```text
The validation checks whether a SKU is present or not in list of SKU's.
```

## Additional Notes
- How can these be specified in such a way that the checkout doesn’t know about items and their pricing strategies?
```text
The promotions for different SKU can be added to promotion strategies. PricingRules is build using the SKU's and promotion.
```
```java
        // Setup promotions for SKU's
        List<PromotionStrategy> promotionStrategies = new ArrayList<>();
        // new promotions can be added for SKU's with "Buy N items for X Price"
        promotionStrategies.add(BuyNItemsWithDiscount.builder().setOfPromoSkuId(new HashSet<>(List.of("A"))).minCountOfSkuToGetDiscount(3).minCountOfSkuWithDiscount(1).discountValue(0.5).build());
        promotionStrategies.add(BuyNItemsWithDiscount.builder().setOfPromoSkuId(new HashSet<>(List.of("B"))).minCountOfSkuToGetDiscount(2).minCountOfSkuWithDiscount(1).discountValue(0.4).build());

        // new promotions can be added for SKU's with "Fixed percentage discount"
        promotionStrategies.add(BuyItemWithFixedDiscount.builder().setOfPromoSkuId(new HashSet<>(List.of("E"))).discountPercentage(10).build());

        // Add SKU's and Promotions to Pricing rules
        PricingRules.builder().skuRepository(skuRepository).promotionStrategies(promotionStrategies).build();
```
- How can we make the design flexible enough so that we can add new styles of pricing rules in the future?
```text
New styles of promotions can be added promotion package. As it follows a Strategy pattern, the promotions are applied at runtime.
```
## To be Implemented ##
Items may be a subject of a discount on a particular date: if you buy on DD.MM.YYYY, the item will cost X % less.