package designs.DistributedCache;
// entry point for our system -->
// user configs the DCS, based on his compute


public class CacheConfig {
    private int numNode;
    private int capacityPerNode; // per cache server 
    private EvictionStrategy evictionStrategy; // strategy design

    public CacheConfig(int numNode, int capacityPerNode, EvictionStrategy evictionStrategy){
        this.numNode = numNode;
        this.capacityPerNode = capacityPerNode;
        this.evictionStrategy = evictionStrategy;
    }   

    public int getNumNode() {
        return numNode;
    }

    public int getCapacityPerNode() {
        return capacityPerNode;
    }

    public EvictionStrategy getEvictionStrategy() {
        return evictionStrategy;
    }
    
}
