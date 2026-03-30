package designs.elevator;

public abstract class ElevatorState {
    protected Elevator elevator;
    public ElevatorState(Elevator elevator){
        this.elevator = elevator;
    }
    public abstract void handleRequest();
    public abstract void openDoor();
    public abstract void closeDoor();
    public abstract void moveTo(int floor);
}
