package designs.ParkingLot;

import java.time.LocalDateTime;

public class TicketService {
    private TicketStore ticketStore;
    public TicketService(TicketStore ticketStore){
        this.ticketStore = ticketStore;
    }

    public ParkingTicket generateTicket(Vehicle vehicle , SlotType slotType, String entryGateId, ParkingSlot assignedSlot, LocalDateTime inTime){ 
        String ticketId = "T- "+entryGateId+"|"+vehicle.getNumberPlate();

        ParkingTicket ticket= new ParkingTicket(ticketId,vehicle, assignedSlot, entryGateId, inTime );
        ticketStore.setTicket(ticket.getTicketId(), ticket);

        return ticket;
    }

    public  ParkingTicket getTicket(String ticketId){
        return ticketStore.getTicket(ticketId);
    }
}
