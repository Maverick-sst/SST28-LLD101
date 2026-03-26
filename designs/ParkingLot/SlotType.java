package designs.ParkingLot;

public enum SlotType {
    SMALL(25.0),
    MEDIUM(50.0),
    LARGE(75.0);

    private final double hourlyRate;

    private SlotType(double i) {
        this.hourlyRate = i;
    }
    public double getHourlyRate(){
        return hourlyRate;
    }
}
