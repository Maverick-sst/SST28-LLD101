package designs.elevator;

public class Door {
    private boolean isOpen;

    public Door() {
        this.isOpen = false;
    }

    public void open() {
        if (!isOpen) {
            isOpen = true;
            System.out.println("Door is now OPEN");
        } else {
            System.out.println("Door is already open");
        }
    }

    public void close() {
        if (isOpen) {
            isOpen = false;
            System.out.println("Door is now CLOSED");
        } else {
            System.out.println("Door is already closed");
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}
