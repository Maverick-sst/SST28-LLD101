package designs.elevator;

public class WaitingState extends ElevatorState {
    public WaitingState(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void handleRequest() {
        System.out.println("request queued");
    }

    @Override
    public void openDoor() {
        System.out.println("Door opens");
    }

    @Override
    public void closeDoor() {
        System.out.println("Door closed");
    }

    @Override
    public void moveTo(int floor) {
        System.out.println("Elevator moving to floor: " + floor);
        elevator.setCurrentFloor(floor);
        elevator.setState(new ActiveState(elevator));
    }
}
