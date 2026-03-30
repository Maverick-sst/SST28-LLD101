package designs.elevator;

public class ActiveState extends ElevatorState {
    public ActiveState(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void handleRequest() {
        System.out.println("Request Queued");
    }

    @Override
    public void openDoor() {
        System.out.println("Opening Doors");
        elevator.openDoors();
        elevator.setState(new WaitingState(elevator));
    }

    @Override
    public void closeDoor() {
        System.out.println("Door closed");
        elevator.closeDoors();
    }

    @Override
    public void moveTo(int floor) {
        System.out.println("Elevator " + elevator.getElevatorId() + " moving to floor: " + floor);
        elevator.setCurrentFloor(floor);
        // Arrived at destination — transition to Idle
        elevator.setState(new IdleState(elevator));
    }
}
