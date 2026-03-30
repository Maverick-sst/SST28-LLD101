package designs.elevator;

public class Display {
    // observer
    private int id; // per elevator
    private int currentFloor;
    private String currentState;
    public Display(int displayId){
        this.id = displayId;
    }
    public void update(int floor, ElevatorState state){
        this.currentFloor = floor ;
        this.currentState = state.getClass().getSimpleName();
        System.out.println("Display [Elevator " + id + "] → Floor: " + floor + " | State: " + currentState);
    }
}
