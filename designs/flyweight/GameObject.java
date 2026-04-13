package designs.flyweight;

public class GameObject {
    // extrinsic state
    private final int x;
    private final int y;
    private final double weight;
    public GameObject(int x, int y, double weight){
        this.x = x;
        this.y= y;
        this.weight = weight;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public double getWeight() {
        return weight;
    }

    
}
