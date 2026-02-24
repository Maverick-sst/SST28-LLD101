import java.util.*;

public class HostelFeeCalculator {
    private final BookingRepo repo;
    private RoomRate room;
    public HostelFeeCalculator(BookingRepo repo,RoomRate room) { this.repo = repo; 
        this.room=room;
    }

    // OCP violation: switch + add-on branching + printing + persistence.
    public void process(BookingRequest req) {
        Money monthly = calculateMonthly(req);
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000)); // deterministic-ish
        repo.save(bookingId, req, monthly, deposit);
    }

    private Money calculateMonthly(BookingRequest req) {
        double base = room.basePrice();
        
        double add = 0.0;
        for (AddOn a : req.addOns) {
            add += a.price();
        }

        return new Money(base + add);
    }
}
