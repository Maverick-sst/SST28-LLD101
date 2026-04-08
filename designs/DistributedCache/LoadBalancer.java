package designs.DistributedCache;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// must be singleton as its being shared across all clients to route request appropriately
public class LoadBalancer {
    private AtomicInteger counter;
    private static volatile LoadBalancer instance;
    private int proxyCount;
    private List<CacheProxy> proxies;

    private LoadBalancer(CacheCluster cluster, HashingStrategy hashingStrategy){
        // it must get initialized with 
        for(int i=0;i<proxyCount;i++){
            proxies.add(new CacheProxy(cluster, hashingStrategy)); // filled the list
        }
    }

    public static LoadBalancer getInstance(CacheCluster cluster,HashingStrategy hashingStrategy){
        if(instance!=null)return instance;
        synchronized(LoadBalancer.class){
            if(instance!=null)return instance;
            instance= new LoadBalancer(cluster, hashingStrategy);
            return instance;
        }
    }
    public CacheProxy routeRequest(){
        // suppose we have 3-5 instances of the cacheproxy which we can use
        // so we can have a .random() which gives us val between thiss range and use the one appropriately
        // using round-robin algo for efficiently distributing traffic across servers

        return proxies.get(counter.getAndUpdate(x -> (x+1) % proxyCount)); // .getAndUpdate returns prev value
        
    }
}
