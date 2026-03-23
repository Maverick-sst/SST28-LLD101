package snakeNladder.dice;

import java.util.Random;

public class Dice {
    private Random random ; // to avoid creating random multiple times across usage of roll()
    public Dice(){
        this.random = new Random();
    }
    public int roll(){
        // gives us random integer values between 1 - 6;
        return 1 + random.nextInt(6);
    }
}
