package designs.DistributedCache;

public class DoubleLinkedList {
    // dummy nodes head & tail
    Node head;
    Node tail;

    public DoubleLinkedList() {
        this.head = new Node(null, null);
        this.tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public void addToHead(Node node) {
        // get the head of curr dll
        Node curr = node;
        Node currPrev = node.prev;
        Node currNext = node.next;
        Node headNext = head.next;

        if (currPrev != null && currNext != null) {
            currPrev.next = currNext;
            currNext.prev = currPrev;
        }

        curr.prev = head;
        head.next = curr;
        curr.next = headNext;
        headNext.prev = curr;

    }

    public Node evict() {
        Node lru = tail.prev;
        lru.prev.next = tail;
        tail.prev = lru.prev;

        // completely disconnect it from its surrounding
        lru.prev = null;
        lru.next = null;
        return lru;
    }
    public void clear(){
        // resetting cache back to normal
        // we dont delete all the nodes inside dll
        // we are simply linking the pointers head & tail back to each other
        // this is called lazy clean up-- O(1)
        // GC can later collect the unreachable nodes and recalim the space back

        head.next = tail;
        tail.prev = head;
    }
}
