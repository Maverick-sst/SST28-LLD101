package designs.elevator;

public class OpenButton extends Button {

    @Override
    public void onPress() {
        // delegate to elevator.openDoors()
        glow();
        getElevator().openDoors();
    }
}
