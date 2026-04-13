package designs.proxy;

public interface ICache {
    String get(String key);
    void put(String key, String value);
    boolean contains(String key);
}
