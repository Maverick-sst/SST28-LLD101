package snakeNladder.engine;

import java.util.Scanner;

import snakeNladder.board.Board;
import snakeNladder.context.SharedContext;
import snakeNladder.dice.Dice;
import snakeNladder.difficultyStrategy.DifficultyStrategy;
import snakeNladder.player.Player;
import snakeNladder.player.PlayerPool;


public class GameEngine {
    private final DifficultyStrategy strategy;
    private final PlayerPool pool;
    private final SharedContext context;
    private final Board board;
    private final Dice dice;
    private final Scanner scanner;
    public GameEngine(DifficultyStrategy strategy, PlayerPool pool, SharedContext context, Board board, Dice dice,Scanner scanner) {
        this.strategy = strategy;
        this.pool = pool;
        this.context = context;
        this.board = board;
        this.dice = dice;
        this.scanner = scanner;
    }

    public void start() {
        int N = board.getSize();
        System.out.println("🏁 Race to cell " + (N * N) + " begins!\n");

        while (!pool.hasGameEnded()) {
            Player p = pool.getNextPlayer();
            // make move from logic
            GameLogic.makeMove(p, dice, context, board, strategy, scanner);
            
            checkWinCondition(p, N);

            if(p.getPosition() != N*N){
                pool.addPlayer(p);
            }
            if(pool.hasGameEnded()){
                System.out.println("----------GAME OVER----------");
                break;
            }

        }
    }
    
    private void checkWinCondition(Player p,int N){
        if(p.getPosition() == N*N){
            System.out.println(p.getSymbol() + " has reached finish!");
            context.removePlayerPosition(p.getSymbol());
        }
    }
   
}
