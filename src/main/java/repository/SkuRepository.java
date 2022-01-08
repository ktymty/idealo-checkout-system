package repository;

import model.Sku;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class SkuRepository {

    private final Map<String, Sku> skuRepository = new ConcurrentHashMap<>();

    public void add(Sku sku) {
        skuRepository.putIfAbsent(sku.getId(), sku);
    }

    public Sku findById(String skuId) throws NoSuchElementException {
        if (!skuRepository.containsKey(skuId))
            throw new NoSuchElementException("No SKU found with id: " + skuId);

        return skuRepository.get(skuId);
    }
}
