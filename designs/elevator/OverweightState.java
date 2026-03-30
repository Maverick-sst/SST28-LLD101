package designs.elevator;

public class OverweightState extends ElevatorState {
    public OverweightState(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void handleRequest() {
        System.out.println("Elevator overweight! Cannot accept requests. Please reduce load.");
    }

    @Override
    public void openDoor() {
        System.out.println("Door staying open — elevator is overweight. Please reduce load.");
        elevator.openDoors();
    }

    @Override
    public void closeDoor() {
        System.out.println("Cannot close door — elevator is overweight. Please reduce load.");
    }

    @Override
    public void moveTo(int floor) {
        System.out.println("Cannot move — elevator is overweight. Please reduce load.");
    }
}
