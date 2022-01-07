package repository;

import model.Sku;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class SkuRepository {

    private final Map<String, Sku> storage = new ConcurrentHashMap<>();

    public void add(Sku sku) {
        storage.putIfAbsent(sku.getId(), sku);
    }

    public Sku findById(String skuId) throws NoSuchElementException {
        if (!storage.containsKey(skuId))
            throw new NoSuchElementException("No SKU found with id: " + skuId);

        return storage.get(skuId);
    }
}
