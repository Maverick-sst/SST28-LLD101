package designs.elevator;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private final int elevatorId;
    private int currentFloor;
    private ElevatorState state;
    private InnerPanel innerPanel;
    private Door door;
    private WeightSensor weightSensor;
    private DoorSensor doorSensor;
    private Display display;

    public Elevator(int elevatorId) {
        this(elevatorId, 10); // default 10 floors
    }

    public Elevator(int elevatorId, int totalFloors) {
        this.elevatorId = elevatorId;
        this.currentFloor = 0;
        this.state = new IdleState(this);
        this.door = new Door();
        this.weightSensor = new WeightSensor(750);
        this.doorSensor = new DoorSensor();
        this.display = new Display(elevatorId);

        // Wire sensors to this elevator
        this.weightSensor.setElevator(this);
        this.doorSensor.setElevator(this);

        // Create floor buttons for all floors
        List<FloorButton> floorButtons = new ArrayList<>();
        for (int i = 0; i < totalFloors; i++) {
            floorButtons.add(new FloorButton(i, this));
        }
        this.innerPanel = new InnerPanel(floorButtons);
        this.innerPanel.bindToElevator(this);
    }

    public void setState(ElevatorState state) {
        this.state = state;
        publishStateChange();
    }

    public void publishStateChange() {
        ElevatorOrchestrator.getInstance().onElevatorStateChange(this);
    }

    public void openDoors() {
        door.open();
    }

    public void closeDoors() {
        door.close();
    }

    public void moveTo(int floor) {
        state.moveTo(floor);
        List<FloorButton> floorButtons = innerPanel.getFloorButtons();
        for (FloorButton btn : floorButtons) {
            if (btn.getFloor() == floor) btn.unglow();
        }
    }

    public void handleSensorEvent(Event e) {
        // observer ---> state
        if (e.getEvent().equals("OVERWEIGHT")) setState(new OverweightState(this));
        else if (e.getEvent().equals("DOOR_OBSTRUCTION")) setState(new WaitingState(this));
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorState getState() {
        return state;
    }

    public Display getDisplay() {
        return display;
    }

    public InnerPanel getInnerPanel() {
        return innerPanel;
    }

    public Door getDoor() {
        return door;
    }

    public WeightSensor getWeightSensor() {
        return weightSensor;
    }

    public DoorSensor getDoorSensor() {
        return doorSensor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}
