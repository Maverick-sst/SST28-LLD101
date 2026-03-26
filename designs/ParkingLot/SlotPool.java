package designs.ParkingLot;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class SlotPool {
    private Map<SlotType, PriorityQueue<ParkingSlot>> slotMap = new HashMap<>();
    
    public SlotPool(){
        for(SlotType slotType : SlotType.values()){
            PriorityQueue<ParkingSlot> currentSlotQueue = new PriorityQueue<>((a,b) -> Double.compare(a.getDistanceFromGate(), b.getDistanceFromGate()));
            slotMap.put(slotType, currentSlotQueue);
        }   
    }

    public void addParkingSlot(SlotType slotType , ParkingSlot slot){
        slotMap.get(slotType).add(slot);
    }

    public ParkingSlot allocateSlot(SlotType type) throws NoSlotAvailableException{
        // allocates slot of given type
        // lock the particular heap
        // poll from heap  keep checking for its avail 
        // as long as it is unavailable
        // once found return 

        // synchronize perSlot per type basis 
        PriorityQueue<ParkingSlot> slots = slotMap.get(type);
        if (slots == null) {
            throw new NoSlotAvailableException();
        }

        synchronized (slots) {
            while (slots.size() > 0 && slots.peek().isOccupied()) {
                slots.poll();
            }

            if (slots.size() == 0) {
                throw new NoSlotAvailableException();
            }
            
            ParkingSlot curr = slots.poll();
            curr.setOccupied(true);
            
            return curr;
        }
    }
    public void releaseSlot(ParkingSlot slot){
        // releases the slot , re adds up the to heap
        // get the slot type out of the curr slot
        // search in the map add back to the heap before adding make sure to swap the isOccupied to false
        
        SlotType type = slot.getSlotType();
        PriorityQueue<ParkingSlot> slots = slotMap.get(type);
        
        synchronized(slots){
            slot.setOccupied(false);
            slots.add(slot);
        }

    }
    public int getAvailableCountByType(SlotType type){
        int count = slotMap.get(type).size();
        return count;
    }
    
} 
