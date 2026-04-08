package designs.DistributedCache;

import java.util.HashMap;
import java.util.Map;

// in terms of number of items that could be stored in one single cache server
// currently not sharing / data duplication is made
// heavy cache invalidation if one goes down

// currently we are building agnostic cacheservers, where 
// data of a particular key is stored only on one node/server 
// to optimize ---> cache coherency----> replicas
public class CacheStore {
    private int capacity;
    Map<String,Node> house;  // unique
    EvictionStrategy evictionStrategy;
    DbService dbService; // fallback to db if data doesn't exist on cache
    CacheService cacheService;
    public CacheStore(int capacity,EvictionStrategy evictionStrategy,DbService dbService){
        this.capacity = capacity;
        this.house = new HashMap<>();
        this.evictionStrategy = evictionStrategy;
        this.dbService=dbService;
        this.cacheService = new CacheService(house, capacity, evictionStrategy, dbService);
    }

    // apis---->
    // in get---> missing value as optional
    public ResponseDTO get(RequestDTO request) throws Exception{
        return cacheService.get(request);
    } 
    // value present in request dto
    public void put(RequestDTO request){
        cacheService.put(request);
    }
    public void clear(){
        cacheService.clear();
    }
}
