package snakeNladder.player;

import java.util.Scanner;

import snakeNladder.context.SharedContext;
import snakeNladder.playerFactory.PlayerFactory;

public class PlayerInput {
    private final Scanner sc;
    private PlayerPool pool;
    private SharedContext context;

    public PlayerInput(Scanner sc, PlayerPool pool, SharedContext context){
        this.sc=sc;
        this.pool = pool;
        this.context = context;
    }
    public void handlePlayerCreation() throws RuntimeException{

        System.out.print("Enter number of players: ");
        int countOfPlayers = sc.nextInt(); // number of players


        // no < 2 retry
        if (countOfPlayers < 2) {
            sc.close();
            throw new RuntimeException("Minimum 2 players required.");
        }
        sc.nextLine();
        for (int i = 0; i < countOfPlayers; i++) {
            System.out.print("Enter symbol for player " + (i + 1) + ": ");
            String symbol = sc.nextLine().trim();
            pool.addPlayer(PlayerFactory.createPlayer(symbol));
            context.updatePlayerPosition(symbol, 1);
        }
    }
}
