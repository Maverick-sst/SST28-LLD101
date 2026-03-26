package designs.ParkingLot;

import java.util.Map;

public interface SlotDistributionStrategy {
    Map<SlotType, Integer> getDistribution();
} 
