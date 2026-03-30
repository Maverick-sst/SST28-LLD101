package designs.elevator;

import java.util.List;

public class InnerPanel {
    private final List<FloorButton> floorButtons;
    private final OpenButton openButton;
    private final CloseButton closeButton;
    private final AlarmButton alarmButton;

    public InnerPanel(List<FloorButton> floorButtons){
        this.floorButtons = floorButtons;
        this.openButton = new OpenButton();
        this.closeButton = new CloseButton();
        this.alarmButton = new AlarmButton();
    }

    public void bindToElevator(Elevator e){
        // bind diff instances of this class across diff elevators
        // each elvator as its own instance
        floorButtons.forEach(btn -> btn.setElevator(e));
        openButton.setElevator(e);
        closeButton.setElevator(e);
        alarmButton.setElevator(e);
    }

    public List<FloorButton> getFloorButtons() {
        return floorButtons;
    }

    public OpenButton getOpenButton() {
        return openButton;
    }

    public CloseButton getCloseButton() {
        return closeButton;
    }

    public AlarmButton getAlarmButton() {
        return alarmButton;
    }
    

}
