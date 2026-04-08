package designs.DistributedCache;

public class SimpleHashStrategy implements HashingStrategy{
    @Override
    public int hash(String key,CacheConfig config) {
        // using simple modular hashing 
        // leads to heavy cache invalidation
        // upon adding / deleting cache servers/nodes
        // thundering herd problem ---> db gets bloated / heavy

        return key.hashCode() % config.getNumNode();
    }
}
