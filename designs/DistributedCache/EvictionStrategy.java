package designs.DistributedCache;

public interface EvictionStrategy {
    void onAccess(Node node);  // for get request
    void onInsert(Node node);  // for put
    Node evict();              // for evicting from tail
    void clear();              // resetting cache
} 