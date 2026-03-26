package designs.ParkingLot;

public class ParkingFullException extends Exception {
    public ParkingFullException() {
        super("ParkingLot Completely Full!!!");

    }
}
