package snakeNladder.teleport;

public class Ladder implements Teleport {
    private final int source;
    private final int destination;
    public Ladder(int source, int destination){
        this.source= source;
        this.destination = destination;
    }

    @Override
    public int teleport() {
        return destination;
    }

    @Override
    public String getSymbol() {
        return "L";
    }
}
