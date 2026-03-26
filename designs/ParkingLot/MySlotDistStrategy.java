package designs.ParkingLot;

import java.util.HashMap;
import java.util.Map;

public class MySlotDistStrategy implements SlotDistributionStrategy{
    @Override
    public Map<SlotType, Integer> getDistribution() {
        // this is where my strategy of deciding which slot should account how much
        // if in future cars give me more profit which as compared to buses or bikes
        // i can revamp the distribution here
        Map<SlotType,Integer> distributionMap= new HashMap<>();
        distributionMap.put(SlotType.SMALL, 40);
        distributionMap.put(SlotType.MEDIUM, 40);
        distributionMap.put(SlotType.LARGE, 20);
        return distributionMap;
    }
}
