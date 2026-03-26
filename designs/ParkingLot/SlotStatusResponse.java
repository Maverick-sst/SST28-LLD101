package designs.ParkingLot;
import java.util.Map;

public class SlotStatusResponse {
    private Map<String, Map<SlotType,Integer>> slotStatusMap;
    public SlotStatusResponse(Map<String,Map<SlotType,Integer>> slotStatusMap){
        this.slotStatusMap = slotStatusMap;
    }
    public Map<String, Map<SlotType,Integer>> getSlotsStatus(){
        return slotStatusMap;
    }
}
