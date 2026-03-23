package snakeNladder.difficultyStrategy;

public class EasyStrategy implements DifficultyStrategy{
    private final int threshold;
    public EasyStrategy(int threshold){
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
