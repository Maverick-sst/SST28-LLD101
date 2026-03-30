package designs.elevator;

public class test {
    public static void main(String[] args) {
        ElevatorOrchestrator instance1 = ElevatorOrchestrator.getInstance();
        ElevatorOrchestrator instance2 = ElevatorOrchestrator.getInstance();
        System.out.println(instance1.hashCode());
        System.err.println(instance2.hashCode());
    }
}
