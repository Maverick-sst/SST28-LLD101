package snakeNladder.engine;

import java.util.Scanner;

import snakeNladder.board.Board; 
import snakeNladder.context.SharedContext;
import snakeNladder.dice.Dice;
import snakeNladder.difficultyStrategy.DifficultyStrategy;
import snakeNladder.player.Player;
import snakeNladder.teleport.Teleport;

public class GameLogic {

    public static void makeMove(Player p, Dice dice, SharedContext context, Board board, DifficultyStrategy strategy, Scanner sc){
        System.out.println("👉 Turn: Player " + p.getSymbol() + " at cell " + p.getPosition());
            boolean continueTurn = true;

            while (continueTurn) {
                int diceVal = handleDiceRoll(sc,dice);
                System.out.println("🎲 " + p.getSymbol() + " rolled: " + diceVal);
                // check remaining steps to prevetn overshoot
                int currPosition = p.getPosition();
                int remainingSteps = board.getSize() * board.getSize() - currPosition;
                if (diceVal > remainingSteps) {
                    System.out.println("⛔ Overshoot! Need exact roll of " + remainingSteps + " to finish.");
                    p.resetSixCounter();
                    continueTurn = false;
                    continue;
                }
                if (diceVal <= remainingSteps) {
                    // valid move , update the position
                    int newPosition = currPosition + diceVal;
                    int finalPosition = checkTeleport(newPosition,context);
                    context.updatePlayerPosition(p.getSymbol(), finalPosition);

                    p.setPosition(finalPosition);

                    board.render(context);

                }
                if (diceVal == 6) {
                    p.incrementConsecutiveSixCounter();
                    if (strategy.isConsecutiveSixes(p.getConsecutiveSixCounter())) {
                        System.out.println("⚠️ Too many consecutive sixes. Turn forfeited.");
                        p.resetSixCounter();
                        continueTurn = false;
                    } else {
                        System.out.println("✨ Rolled a 6! Bonus roll.");
                    }
                    // else continueTurn stays true, player gets to roll again
                } else {
                    p.resetSixCounter();
                    continueTurn = false; // non-6 always ends turn
                }
        
            }
    }

    private static int checkTeleport(int currPosition,SharedContext context){
        Teleport t =context.getTeleport(currPosition);
        if (t!=null) {
            if ("S".equals(t.getSymbol())) {
                System.out.println("🐍 Oops! Snake bite at " + currPosition + ". Sliding down...");
            } else if ("L".equals(t.getSymbol())) {
                System.out.println("🪜 Nice! Ladder at " + currPosition + ". Climbing up...");
            }
            return t.teleport();
        }
        return currPosition;
        
    }
    
    private static int handleDiceRoll(Scanner sc, Dice dice) {
        System.out.print("Press Enter to roll!");
        sc.nextLine();
        return dice.roll();
    }
}
