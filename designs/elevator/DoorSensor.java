package designs.elevator;

public class DoorSensor {
    // publisher
    private Elevator elevator;
    private boolean isObstructed;

    public DoorSensor() {
        this.isObstructed = false;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public void detectObstruction() {
        this.isObstructed = true;
        publish(new Event("DOOR_OBSTRUCTION"));
    }

    public void clearObstruction() {
        this.isObstructed = false;
    }

    public boolean isObstructed() {
        return isObstructed;
    }

    public void publish(Event e) {
        if (elevator != null) {
            elevator.handleSensorEvent(e);
        }
    }
}
