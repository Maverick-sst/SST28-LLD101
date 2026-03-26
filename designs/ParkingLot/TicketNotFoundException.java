package designs.ParkingLot;

public class TicketNotFoundException extends Exception {
    public TicketNotFoundException(){
        super("Ticket not found!");
    }
}
