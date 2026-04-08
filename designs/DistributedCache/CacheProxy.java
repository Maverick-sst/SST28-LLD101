package designs.DistributedCache;

public class CacheProxy {
    private final CacheRouter router;

    public CacheProxy(CacheCluster cluster, HashingStrategy hashingStrategy) {
        this.router = CacheRouter.getRouter(cluster, hashingStrategy); // singelton
    }

    // exposing apis + handle fallback to dbservice/ cacheService---> cache miss
    // internally
    // per cache node/server---> dbservice
    public ResponseDTO get(RequestDTO request) throws Exception {
        CacheNode cacheNode = router.routeRequest(request);
        return cacheNode.get(request);
    }

    public void put(RequestDTO request) {
        CacheNode cacheNode = router.routeRequest(request);
        cacheNode.set(request);
    }
}
