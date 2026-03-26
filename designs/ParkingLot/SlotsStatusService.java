package designs.ParkingLot;

import java.util.HashMap;
import java.util.Map;

public class SlotsStatusService {
    private final Map<String, Floor> floorMap;

    public SlotsStatusService(Map<String, Floor> floorMap) {
        this.floorMap = floorMap;
    }

    public SlotStatusResponse getStatus() {
        Map<String, Map<SlotType, Integer>> slotStatusMap = new HashMap<>();

        for (Floor floor : floorMap.values()) {
            HashMap<SlotType, Integer> internalMap = new HashMap<>();
            for (SlotType slotType : SlotType.values()) {
                internalMap.put(slotType, floor.getAvailableCountByType(slotType));
            }
            slotStatusMap.put(floor.getFloorId(), internalMap);
        }

        return  new SlotStatusResponse(slotStatusMap);
    }
}
