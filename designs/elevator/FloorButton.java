package designs.elevator;

public class FloorButton extends Button {
    private final int floorNo;

    public FloorButton(int floorNo, Elevator elevator) {
        this.floorNo = floorNo;
        setElevator(elevator);
    }

    @Override
    public void onPress() {
        // upon pressing should trigger elevator.moveTo(floor)
        glow();
        getElevator().moveTo(floorNo);
    }

    public int getFloor() {
        return floorNo;
    }
}
