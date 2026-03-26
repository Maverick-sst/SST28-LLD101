package designs.ParkingLot;

public interface SlotAllocationStrategy {
    ParkingSlot getNearestSlot(SlotType slotType) throws NoSlotAvailableException;
} 