package designs.ParkingLot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class BillingService {
    private final Map<String, Floor> floorMap;

    public BillingService(Map<String, Floor> floorMap){
        this.floorMap = floorMap;
    }

    public ParkingBill generateBill(ParkingTicket ticket, LocalDateTime outTime)throws TicketNotFoundException{

        // first calculate the total cost
        // then appropriately release the slot back to the pool
        Duration duration = Duration.between(ticket.getInTime(), outTime);
        long totalMins = duration.toMinutes();
        double Hours = (double) totalMins / 60 ;
        double total = Hours * ticket.getAssignedSlot().getSlotType().getHourlyRate();

        // use floormap to releease the slot
        floorMap.get(ticket.getAssignedSlot().getFloorId()).releaseSlot(ticket.getAssignedSlot());
        return new ParkingBill(ticket, outTime, total);
    }
}
