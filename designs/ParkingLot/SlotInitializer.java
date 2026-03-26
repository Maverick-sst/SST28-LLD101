package designs.ParkingLot;
import java.util.Map;


public class SlotInitializer {
    private final int rows;
    private final int cols;
    private final SlotDistributionStrategy strategy;
    private final SlotPool pool;
    private final EntryGate entryGate;
    private final String floorId;
    public SlotInitializer(int rows , int cols, SlotDistributionStrategy strategy, SlotPool pool, EntryGate entryGate, String floorId){
        this.rows = rows;
        this.cols = cols;
        this.strategy = strategy;
        this.pool= pool;
        this.entryGate = entryGate;
        this.floorId = floorId;
    }  

    public ParkingSlot[][] initialize(){
        // create a grid get slots in right order from enum class
        // for each slot type, iterate over its count from strategy
        // create the row and col appropriately in the grid
        // row --> index / cols and col ----> index% cols
        
        ParkingSlot[][] grid = new ParkingSlot[rows][cols];
        Map<SlotType,Integer> distributionMap = strategy.getDistribution();

        int idx =0;
        for(SlotType slotType: SlotType.values()){
            int countOfSlots = distributionMap.get(slotType);

            for(int i=0;i<countOfSlots;i++){
                int row = idx/cols , col= idx%cols;

                String slotId = slotType + " - "+ i;

                double x = Math.pow((row - entryGate.getRow()), 2);
                double y = Math.pow((col - entryGate.getCol()), 2);

                double distanceFromGate = Math.sqrt(x + y);
                ParkingSlot currentSlot = new ParkingSlot(slotId, row, col, slotType,floorId,distanceFromGate);
                pool.addParkingSlot(slotType, currentSlot);
                grid[row][col]=currentSlot;
                idx++;
            }
        }
        return grid;
    }
}
