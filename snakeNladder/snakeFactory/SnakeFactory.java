package snakeNladder.snakeFactory;

import java.util.Random;
import java.util.Set;

import snakeNladder.context.SharedContext;
import snakeNladder.teleport.Snake;
import snakeNladder.teleport.Teleport;

public class SnakeFactory {
    public static void createSnakes(int count,SharedContext context, Set<Integer> occupiedCells){
        int snakesCreated = 0;
        Random random = new Random();
        int maxCell = count * count;

        while (snakesCreated < count) {
            int head = 2 + random.nextInt(maxCell - 2);
            int tail = 1 + random.nextInt(head - 1);
            if (occupiedCells.contains(head) || occupiedCells.contains(tail)) {
                continue;
            }

            Teleport snake = new Snake(head, tail);
            context.registerTeleport(head, snake);

            // mark them as occupied
            occupiedCells.add(head);
            occupiedCells.add(tail);
            snakesCreated++;

        }
    } 
}
