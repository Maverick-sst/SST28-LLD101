package snakeNladder.playerFactory;

import snakeNladder.player.Player;

public class PlayerFactory {
    public static Player createPlayer(String symbol){
        return new Player(symbol, 1, 0);
    }
}
