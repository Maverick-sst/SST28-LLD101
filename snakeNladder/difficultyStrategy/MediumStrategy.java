package snakeNladder.difficultyStrategy;

public class MediumStrategy implements DifficultyStrategy {
    private final int threshold;
    public MediumStrategy(int threshold){
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
