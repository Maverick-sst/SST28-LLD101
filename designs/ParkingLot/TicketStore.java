package designs.ParkingLot;

import java.util.HashMap;

public class TicketStore {
    private  HashMap<String, ParkingTicket> ticketStore;
    public TicketStore(){
        ticketStore = new HashMap<>();
    }
    public void setTicket(String ticketId, ParkingTicket ticket){
        ticketStore.put(ticketId, ticket);
    }
    public ParkingTicket getTicket(String ticketId){
        return ticketStore.get(ticketId);
    }
}
