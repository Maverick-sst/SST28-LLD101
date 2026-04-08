package designs.DistributedCache;

import java.util.Map;

public class CacheService {
    Map<String,Node> house;
    int capacity;
    EvictionStrategy evictionStrategy;
    DbService dbService;

    public CacheService(Map<String,Node> house,int capacity,EvictionStrategy evictionStrategy, DbService dbService){
        this.house = house;
        this.capacity = capacity;
        this.evictionStrategy = evictionStrategy;
        this.dbService = dbService;
    }

    public ResponseDTO get(RequestDTO request) throws Exception{
        // handle fallback 
        Node node = house.get(request.getKey());
        String value = null;
        if(node == null){
            value = dbService.executeQuery(request.getKey());
            return new ResponseDTO(request.getKey(), value);
        }
        value = node.getValue();
        // handling dll reordering
        evictionStrategy.onAccess(node);

        ResponseDTO response = new ResponseDTO(request.getKey(),value);
        return response;
    }
    public void put(RequestDTO request){
        Node node = new Node(request.getKey(), request.getValue());
        evictionStrategy.onInsert(node);
        // we have to handle it appropriately on our end
        if(house.size() == capacity){
            Node lru =remove();
            house.remove(lru.getKey()); // removed mapping for that lru key
        }
        house.put(request.getKey(), node);
    }
    private Node remove(){
        Node lru = evictionStrategy.evict();
        return lru;
    }

    public void clear(){
        // to clear the whole cache / reset it
        evictionStrategy.clear();
    }
}
