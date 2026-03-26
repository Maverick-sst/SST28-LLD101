package designs.ParkingLot;

import java.time.LocalDateTime;

public class ParkingBill {
    private final ParkingTicket ticket;
    private final LocalDateTime outTime;
    private final double totalCost;

    public ParkingBill(ParkingTicket ticket, LocalDateTime ouTime, double totalCost){
        this.ticket = ticket;
        this.outTime = ouTime;
        this.totalCost = totalCost;
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

    public double getTotalCost() {
        return totalCost;
    }
    
}
