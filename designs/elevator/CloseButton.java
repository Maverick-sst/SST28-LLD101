package designs.elevator;

public class CloseButton extends Button {

    @Override
    public void onPress() {
        // delegate to elevator.closeDoors()
        glow();
        getElevator().closeDoors();
    }
}
