package designs.elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorOrchestrator {
    // singleton observer
    private static volatile ElevatorOrchestrator instance;
    private final List<Elevator> elevators;
    // DA---> algo
    private AllocationStrategy strategy;

    private ElevatorOrchestrator() {
        this.elevators = new ArrayList<>();
        this.strategy = new NearestElevatorStrategy(); 
    }

    public static ElevatorOrchestrator getInstance() {
        if (instance == null) {
            synchronized (ElevatorOrchestrator.class) {
                // double check locking
                if (instance == null) {
                    instance = new ElevatorOrchestrator();
                }
            }
        }
        return instance;
    }

    // For testing: reset the singleton
    public static synchronized void resetInstance() {
        instance = null;
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public void handleRequest(Floor floor, Direction direction) {
        ElevatorRequest req = new ElevatorRequest(floor.getFloorId(), direction, System.currentTimeMillis());
        Elevator allocated = strategy.allocate(elevators, req);
        if (allocated != null) {
            System.out.println("Allocating Elevator " + allocated.getElevatorId() + " to floor " + req.getFloorNo());
            allocated.moveTo(req.getFloorNo());
        } else {
            System.out.println("No elevator available for request at floor " + req.getFloorNo());
        }
    }

    public void onElevatorStateChange(Elevator e) {
        // push notification architecture
        // rather than pulling constantly 
        // tight coupling but is efficient for our design
        notifyDisplay(e);
    }

    public void setStrategy(AllocationStrategy strategy){
        this.strategy = strategy;
    }

    public void notifyDisplay(Elevator e){
        Display display = e.getDisplay();
        display.update(e.getCurrentFloor(), e.getState());
    }
}
