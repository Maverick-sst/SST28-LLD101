package designs.proxy;

import java.util.HashMap;
import java.util.Map;

public class CacheService implements ICache{
    private final Map<String,String> cache = new HashMap<>();

    @Override
    public String get(String key) {
        return cache.get(key);
    }
    @Override
    public void put(String key, String value) {
        cache.put(key, value);
    }
    @Override
    public boolean contains(String key) {
        return cache.containsKey(key);
    }

}
