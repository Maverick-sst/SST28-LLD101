package designs.elevator;

public abstract class Button {
    private boolean isPressed;
    private Elevator elevator;

    public abstract void onPress();

    public void glow() {
        isPressed = true;
        System.out.println("Button Glowing RED");
    }

    public void unglow() {
        isPressed = false;
        System.out.println("Button Becomes NORMAL");
    }

    public void setElevator(Elevator e) {
        this.elevator = e;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public boolean isPressed() {
        return isPressed;
    }
}
