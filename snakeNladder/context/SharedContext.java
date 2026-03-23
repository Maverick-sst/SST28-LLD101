package snakeNladder.context;

import java.util.HashMap;

import snakeNladder.teleport.Teleport;

public class SharedContext {
    private HashMap<Integer, Teleport> teleportMap;
    private HashMap<String, Integer> playerPositions;

    public SharedContext(HashMap<Integer, Teleport> teleportMap, HashMap<String, Integer> playerPositions) {
        this.teleportMap = teleportMap;
        this.playerPositions = playerPositions;
    }

    public void registerTeleport(int position, Teleport t) {
        this.teleportMap.put(position, t);
    }

    public Teleport getTeleport(int position) {
        return this.teleportMap.get(position);
    }

    public boolean updatePlayerPosition(String symbol, int position) {
        if (this.playerPositions.put(symbol, position) != null) {
            return true;
        }
        return false;
    }
    public Integer removePlayerPosition(String symbol){
        return this.playerPositions.remove(symbol);
    }

    public HashMap<String, Integer> getPlayerPositions() {
        return this.playerPositions;
    }
}
