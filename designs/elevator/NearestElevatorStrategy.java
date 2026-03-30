package designs.elevator;

import java.util.List;

public class NearestElevatorStrategy implements AllocationStrategy {
    
    @Override
    public Elevator allocate(List<Elevator> elevators, ElevatorRequest req) {
        Elevator best = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            // skip unavailable elevators
            if (e.getState() instanceof MaintenanceState || 
                e.getState() instanceof OverweightState) continue;

            int distance = Math.abs(e.getCurrentFloor() - req.getFloorNo());
            if (distance < minDistance) {
                minDistance = distance;
                best = e;
            }
        }
        return best;
    }
}