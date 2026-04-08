package designs.DistributedCache;

public class Node {
    private String key;
    private String value;
    protected Node prev;
    protected Node next;
    
    public Node(String key,String value){
        this.key = key;
        this.value = value;
        this.prev = null;
        this.next = null;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


}
