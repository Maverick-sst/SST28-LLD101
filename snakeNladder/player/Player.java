package snakeNladder.player;

public class Player {
    private final String symbol;
    private int Position;
    private int consecutiveSixCounter;

    public Player(String symbol, int linearPosition, int consecutiveSixCounter){
        this.symbol=symbol;
        this.Position = linearPosition;
        this.consecutiveSixCounter = consecutiveSixCounter;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPosition() {
        return Position;
    }
    public void setPosition(int position){
        this.Position = position;
    }
    public int getConsecutiveSixCounter() {
        return consecutiveSixCounter;
    }
    public void incrementConsecutiveSixCounter(){
        this.consecutiveSixCounter++;
    }
    public void resetSixCounter(){
        this.consecutiveSixCounter = 0;
    }

    public void setConsecutiveSixCounter(int consecutiveSixCounter) {
        this.consecutiveSixCounter = consecutiveSixCounter;
    }
    
}
