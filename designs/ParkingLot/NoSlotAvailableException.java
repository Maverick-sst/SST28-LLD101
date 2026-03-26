package designs.ParkingLot;

public class NoSlotAvailableException extends Exception {
    public NoSlotAvailableException(){
        super("No slot is free at the moment");
    }
}
