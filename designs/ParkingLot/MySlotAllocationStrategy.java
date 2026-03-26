package designs.ParkingLot;

public class MySlotAllocationStrategy implements SlotAllocationStrategy{
    private SlotPool pool;
    public MySlotAllocationStrategy(SlotPool pool){
        this.pool = pool;
    }
    @Override
    public ParkingSlot getNearestSlot(SlotType slotType) throws NoSlotAvailableException{
        return pool.allocateSlot(slotType);
    }
}
