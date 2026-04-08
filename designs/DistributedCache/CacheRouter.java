package designs.DistributedCache;

import java.util.List;

// singleton 
public class CacheRouter {
    private static volatile CacheRouter router;
    private final HashingStrategy hashingStrategy;
    private CacheConfig config;
    private List<CacheNode> cacheServers;

    private CacheRouter(CacheCluster cluster, HashingStrategy hashingStrategy) {
        this.hashingStrategy = hashingStrategy;
        this.cacheServers = cluster.getCacheNodes();

    }

    public static CacheRouter getRouter(CacheCluster cluster, HashingStrategy hashingStrategy) {
        if (router != null)
            return router;
        synchronized (CacheRouter.class) {
            if (router != null)
                return router;
            router = new CacheRouter(cluster, hashingStrategy);
            return router;
        }
    }

    // cacheRouter ---> specific Cache Server
    public CacheNode routeRequest(RequestDTO request) {
        int cacheServerId = hashingStrategy.hash(request.getKey(), config);
        return cacheServers.get(cacheServerId);
    }

}
