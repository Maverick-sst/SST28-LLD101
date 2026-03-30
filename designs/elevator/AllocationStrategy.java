package designs.elevator;

import java.util.List;

public interface AllocationStrategy {
    Elevator allocate(List<Elevator> elevators, ElevatorRequest req);
} 