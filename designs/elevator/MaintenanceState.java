package designs.elevator;

public class MaintenanceState extends ElevatorState {

    public MaintenanceState(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void handleRequest() {
        System.out.println("Elevator is under maintenance. Cannot accept requests.");
    }

    @Override
    public void openDoor() {
        System.out.println("Elevator is under maintenance. Door operation disabled.");
    }

    @Override
    public void closeDoor() {
        System.out.println("Elevator is under maintenance. Door operation disabled.");
    }

    @Override
    public void moveTo(int floor) {
        System.out.println("Elevator is under maintenance. Cannot move.");
    }
}
