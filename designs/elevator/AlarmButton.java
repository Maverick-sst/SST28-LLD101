package designs.elevator;

public class AlarmButton extends Button {

    @Override
    public void onPress() {
        // triggers alarm — glows and alerts
        glow();
        System.out.println("ALARM triggered! Emergency services notified.");
    }
}
