package designs.DistributedCache;

import java.util.List;

public class CacheCluster {
    private List<CacheNode> cacheServers;

    public CacheCluster(CacheConfig config, EvictionStrategy evictionStrategy, DbService dbService) {
        for (int i = 0; i < config.getNumNode(); i++) {
            cacheServers.add(new CacheNode(config, evictionStrategy, dbService));
        }
    }

    public List<CacheNode> getCacheNodes(){
        return cacheServers;
    }
}