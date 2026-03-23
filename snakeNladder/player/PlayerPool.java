package snakeNladder.player;

import java.util.Queue;

public class PlayerPool {
    private final Queue<Player> pool;
    // private int initialSize;
    public PlayerPool(Queue<Player> pool){
        this.pool = pool;
    }

    public boolean addPlayer(Player p){
        return this.pool.add(p);
    }

    public Player getNextPlayer(){
        return this.pool.remove();
    }

    public Boolean eliminatePlayer(Player p){
        return this.pool.remove(p);
    }
    public boolean hasGameEnded(){
        return this.pool.size() == 0;
    }
    public int getSize(){
        return this.pool.size();
    }


}
