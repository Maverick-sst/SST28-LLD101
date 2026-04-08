package designs.DistributedCache;

public class CacheNode {

    private CacheStore cacheStore;

    public CacheNode(CacheConfig config, EvictionStrategy evictionStrategy, DbService dbService) {
        this.cacheStore = new CacheStore(config.getCapacityPerNode(), evictionStrategy, dbService);
    }
    // apis--->

    public ResponseDTO get(RequestDTO request) throws Exception {
        return cacheStore.get(request);
    }

    public void set(RequestDTO request) {
        cacheStore.put(request);
    }
}
