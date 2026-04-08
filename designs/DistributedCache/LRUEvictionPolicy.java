package designs.DistributedCache;

public class LRUEvictionPolicy implements EvictionStrategy{
    // per cacheNode ---> cacheStore
    // dll
    private DoubleLinkedList dll ;
    public LRUEvictionPolicy(){
        this.dll = new DoubleLinkedList();
    }
    @Override
    public void onAccess(Node node) {
        dll.addToHead(node);
    }

    @Override
    public void onInsert(Node node) {
        dll.addToHead(node);
    }

    @Override
    public Node evict() {
        return dll.evict();
    }
    @Override
    public void clear() {
        dll.clear();
    }
    
}
