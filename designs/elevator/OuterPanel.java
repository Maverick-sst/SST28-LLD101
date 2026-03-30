package designs.elevator;

public class OuterPanel {
    private DirectionalButton upButton;
    private DirectionalButton downButton;
    private Floor floor;

    public OuterPanel(Floor floor) {
        this.floor = floor;
        this.upButton = new DirectionalButton(floor, Direction.UP);
        this.downButton = new DirectionalButton(floor, Direction.DOWN);
    }

    public void pressUp() {
        upButton.onPress();
    }

    public void pressDown() {
        downButton.onPress();
    }

    public DirectionalButton getUpButton() {
        return upButton;
    }

    public DirectionalButton getDownButton() {
        return downButton;
    }

    public Floor getFloor() {
        return floor;
    }
}
