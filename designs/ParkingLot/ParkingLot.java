package designs.ParkingLot;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

// singleton
public class ParkingLot {
    private static volatile ParkingLot parkingLot; // 100% thread safe
    private final LinkedHashMap<String, Floor> floorMap;

    private final TicketService ticketService;
    private final BillingService billingService;
    private final SlotsStatusService slotsStatusService;
    private ParkingLot() {
        this.floorMap = new LinkedHashMap<>();
        this.ticketService = new TicketService(new TicketStore());
        this.billingService = new BillingService(floorMap);
        this.slotsStatusService=new SlotsStatusService(floorMap);
    }

    public static ParkingLot getInstance() {
        // thread safe ----> double check locking
        if (parkingLot == null) {
            synchronized (ParkingLot.class) {
                if (parkingLot != null)
                    return parkingLot;
                parkingLot = new ParkingLot();
                return parkingLot;
            }
        }
        return parkingLot;
    }

    public void addFloor(Floor floor) {
        floorMap.put(floor.getFloorId(), floor);
    }

    public String getAvailableEntryGate(SlotType slotType) throws ParkingFullException {
        for (Floor floor : floorMap.values()) {
            if (floor.getAvailableCountByType(slotType) > 0) {
                return floor.getEntryGate().getGateId();
            }
        }
        throw new ParkingFullException();

    }

    public ParkingTicket generateTicket(Vehicle vehicle,SlotType slotType,String entryGateId,LocalDateTime inTime) throws NoSlotAvailableException {
        Floor floor = floorMap.get(entryGateId);
        ParkingSlot slot = floor.getNearestSlot(slotType);

        return ticketService.generateTicket(vehicle, slotType, entryGateId, slot,inTime);

    }

    public ParkingBill generateBill(String ticketId, LocalDateTime outTime) throws TicketNotFoundException {
        ParkingTicket ticket = ticketService.getTicket(ticketId);
        return billingService.generateBill(ticket,outTime);
    }

    public SlotStatusResponse showStatus(){
        return slotsStatusService.getStatus();
    }
}
