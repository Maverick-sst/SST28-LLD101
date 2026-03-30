package designs.elevator;

public class DirectionalButton extends Button {
    private Floor floor;
    private Direction direction;

    public DirectionalButton() {
        // default no-arg constructor for OuterPanel initialization
    }

    public DirectionalButton(Floor floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Floor getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void onPress() {
        // upon pressing outer panel btn
        // delegate to orchestrator.handleRequest()
        glow();
        if (floor != null && direction != null) {
            ElevatorOrchestrator.getInstance().handleRequest(floor, direction);
        }
    }
}