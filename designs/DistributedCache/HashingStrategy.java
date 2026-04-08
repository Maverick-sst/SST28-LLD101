package designs.DistributedCache;

// interface --> as currently using basic round-robin
// later on can be replaced with more sophisticated 
// consistent hashing alogs

public interface HashingStrategy {  
    int hash(String key, CacheConfig config);
}