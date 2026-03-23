package snakeNladder;

import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import snakeNladder.board.Board;
import snakeNladder.context.SharedContext;
import snakeNladder.dice.Dice;
import snakeNladder.difficultyStrategy.DifficultyStrategy;
import snakeNladder.difficultyStrategy.StrategySelector;
import snakeNladder.ladderFactory.LadderFactory;
import snakeNladder.player.Player;
import snakeNladder.player.PlayerInput;
import snakeNladder.player.PlayerPool;
import snakeNladder.snakeFactory.SnakeFactory;
import snakeNladder.teleport.Teleport;
import snakeNladder.engine.GameEngine;

public class GameApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // game init starts here
        System.out.println("======================================");
        System.out.println("       WELCOME TO SNAKES & LADDERS 🪜");
        System.out.println("======================================");

        System.out.print("Enter board size N (for N x N board): ");
        int boardSize = sc.nextInt(); // n*n board
        Board board = new Board(boardSize);
        HashMap<Integer, Teleport> teleportMap = new HashMap<>();
        HashMap<String, Integer> playerPositions = new HashMap<>();

        SharedContext context = new SharedContext(teleportMap, playerPositions);
        Queue<Player> queue = new LinkedList<>();
        PlayerPool pool = new PlayerPool(queue);
        PlayerInput playerInput = new PlayerInput(sc, pool, context);

        try {
            playerInput.handlePlayerCreation();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        DifficultyStrategy strategy = StrategySelector.selectStrategy(sc);

        // creating N snakes and ladders
        Set<Integer> occupiedCells = new HashSet<>();
        // snakes created at init
        SnakeFactory.createSnakes(boardSize,context,occupiedCells);

        // ladders created at init
        LadderFactory.createLadders(boardSize, context, occupiedCells);

        // create instance of dice
        Dice dice = new Dice();

        // initializing the Game Engine to start the game
        GameEngine game = new GameEngine(strategy, pool, context, board, dice,sc);

        System.out.println("\n🎮 Game setup complete! Starting match...\n");
        game.start();
        sc.close();

    }
}
