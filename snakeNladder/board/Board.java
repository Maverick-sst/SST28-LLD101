package snakeNladder.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import snakeNladder.context.SharedContext;

public class Board {
    private final int rows;

    public Board(int N) {
        this.rows = N;
    }

    public void render(SharedContext context) {
        int N = rows;
        System.out.println("\n📍 Board Snapshot");
        Map<String, Integer> playerPositions = context.getPlayerPositions();

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                // converting row,col to linear position
                int linearPosition;
                if (row % 2 == 0) {
                    linearPosition = (N - 1 - row) * N + col + 1;
                } else {
                    linearPosition = (N - 1 - row) * N + (N - 1 - col) + 1;
                }
                
                List<String> playersAtCell = new ArrayList<>();
                for(Map.Entry<String, Integer> entry : playerPositions.entrySet()){
                    if(entry.getValue() == linearPosition){
                        playersAtCell.add(entry.getKey());
                    }
                }
                if(playersAtCell.size() > 0){
                    System.out.print(String.join(",", playersAtCell));
                }else if(context.getTeleport(linearPosition)!= null){
                    System.out.print(context.getTeleport(linearPosition).getSymbol());
                }else{
                    System.out.print("-");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public int getSize() {
        return this.rows;
    }
}
