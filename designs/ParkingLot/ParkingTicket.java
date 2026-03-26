package designs.ParkingLot;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot assignedSlot;
    private final String entryGateId;
    private final LocalDateTime inTime;

    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSlot assignedSlot, String entryGateId, LocalDateTime inTime ){
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.assignedSlot = assignedSlot;
        this.entryGateId = entryGateId;
        this.inTime = inTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getAssignedSlot() {
        return assignedSlot;
    }

    public String getEntryGateId() {
        return entryGateId;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }


}
