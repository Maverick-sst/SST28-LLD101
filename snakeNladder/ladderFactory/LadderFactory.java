package snakeNladder.ladderFactory;

import java.util.Random;
import java.util.Set;

import snakeNladder.context.SharedContext;
import snakeNladder.teleport.Ladder;
import snakeNladder.teleport.Teleport;

public class LadderFactory {
    public static void createLadders(int count, SharedContext context,Set<Integer> occupiedCells){
        Random random = new Random();
        int maxCell = count * count;
        int laddersCreated = 0;

        while (laddersCreated < count) {
            int base = 1 + random.nextInt(maxCell - 1);
            int top = base + 1 + random.nextInt(maxCell - base);

            if (occupiedCells.contains(base) || occupiedCells.contains(top)) {
                continue;
            }
            Teleport ladder = new Ladder(base, top);
            context.registerTeleport(base, ladder);

            // mark it as present
            occupiedCells.add(base);
            occupiedCells.add(top);
            laddersCreated++;
        }
    }
}
