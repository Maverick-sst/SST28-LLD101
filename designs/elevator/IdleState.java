package designs.elevator;

public class IdleState extends ElevatorState{

    public IdleState(Elevator elevator){
        super(elevator);
    }
    @Override
    public void handleRequest() {
        System.out.println("Idle elevator accepting request");
    }

    @Override
    public void openDoor() {
        System.out.println("Opening door from idle");
        elevator.setState(new WaitingState(elevator));
    }

    @Override
    public void closeDoor() {
        System.out.println("Already idle, door already closed");
    }

    @Override
    public void moveTo(int floor) {
        System.out.println("Elevator moving to floor: "+ floor);
        elevator.setCurrentFloor(floor);
        elevator.setState(new ActiveState(elevator));
    }
    
}
