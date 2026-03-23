package snakeNladder.difficultyStrategy;

import java.util.Scanner;

public class StrategySelector {
    
    public static DifficultyStrategy selectStrategy(Scanner sc){
        // selecting difficulty
        System.out.println("\nChoose Difficulty:");
        System.out.println("  1) Easy");
        System.out.println("  2) Medium");
        System.out.println("  3) Hard");
        System.out.print("Enter choice: ");

        int level = sc.nextInt();
        // creating appropriate strategy object
        sc.nextLine();
        DifficultyStrategy strategy;
        if (level == 1) {
            strategy = new EasyStrategy(Integer.MAX_VALUE);
        } else if (level == 2) {
            strategy = new MediumStrategy(6);
        } else {
            strategy = new HardStrategy(3);
        }
        return strategy;
    }
}
