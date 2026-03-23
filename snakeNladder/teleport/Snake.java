package snakeNladder.teleport;

public class Snake implements Teleport {
    private final int source;
    private final int destination;

    public Snake(int source, int destination){
        this.source = source;
        this.destination = destination;
    }
    @Override
    public int teleport() {
        return this.destination;
    }
    @Override
    public String getSymbol() {
        return "S";
    }
}
