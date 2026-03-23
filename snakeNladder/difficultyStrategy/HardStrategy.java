package snakeNladder.difficultyStrategy;

public class HardStrategy implements DifficultyStrategy{
    private final int threshold;
    public HardStrategy(int threshold){
        this.threshold = threshold;
    }
    @Override
    public boolean isConsecutiveSixes(int count) {
        if (count >= this.threshold) {
            return true;
        }
        return false;
    }
}
