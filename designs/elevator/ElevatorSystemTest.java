package designs.elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comprehensive test suite for the Elevator System LLD.
 * Tests all design patterns, state transitions, concurrency,
 * sensors, buttons, and request-allocation flows.
 */
public class ElevatorSystemTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║         ELEVATOR SYSTEM — COMPREHENSIVE TESTS       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        // Reset singleton before every test group
        testSingletonPattern();
        testSingletonThreadSafety();
        testElevatorInitialization();
        testStateTransitions();
        testDoorOperations();
        testSensorEvents();
        testOverweightState();
        testMaintenanceState();
        testNearestElevatorStrategy();
        testOrchestratorRequestFlow();
        testFloorAndOuterPanel();
        testInnerPanelButtons();
        testDisplayObserver();
        testMultipleElevators();
        testConcurrentRequests();
        testConcurrentElevatorMovement();
        testStrategySwap();

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf("║  RESULTS: %d PASSED | %d FAILED | %d TOTAL            ║%n", passed, failed, passed + failed);
        System.out.println("╚══════════════════════════════════════════════════════╝");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // ─────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  ✅ PASSED: " + testName);
        } else {
            failed++;
            System.out.println("  ❌ FAILED: " + testName);
        }
    }

    private static void section(String title) {
        System.out.println("\n━━━ " + title + " ━━━");
    }

    private static void resetOrchestrator() {
        ElevatorOrchestrator.resetInstance();
    }

    // ─────────────────────────────────────────────────────────
    // 1. Singleton Pattern
    // ─────────────────────────────────────────────────────────

    private static void testSingletonPattern() {
        section("1. Singleton Pattern");
        resetOrchestrator();

        ElevatorOrchestrator a = ElevatorOrchestrator.getInstance();
        ElevatorOrchestrator b = ElevatorOrchestrator.getInstance();

        check("getInstance() returns same instance", a == b);
        check("hashCode matches", a.hashCode() == b.hashCode());
    }

    // ─────────────────────────────────────────────────────────
    // 2. Singleton Thread-Safety (DCL)
    // ─────────────────────────────────────────────────────────

    private static void testSingletonThreadSafety() throws Exception {
        section("2. Singleton Thread-Safety (Double-Check Locking)");
        resetOrchestrator();

        int threadCount = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        List<ElevatorOrchestrator> instances = java.util.Collections.synchronizedList(new ArrayList<>());
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                try {
                    startLatch.await(); // all threads start at once
                    instances.add(ElevatorOrchestrator.getInstance());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // fire!
        doneLatch.await(5, TimeUnit.SECONDS);
        pool.shutdown();

        ElevatorOrchestrator expected = instances.get(0);
        boolean allSame = instances.stream().allMatch(inst -> inst == expected);
        check(threadCount + " threads all get the same singleton", allSame);
        check("Exactly " + threadCount + " references collected", instances.size() == threadCount);
    }

    // ─────────────────────────────────────────────────────────
    // 3. Elevator Initialization
    // ─────────────────────────────────────────────────────────

    private static void testElevatorInitialization() {
        section("3. Elevator Initialization");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        check("Elevator ID is 1", elevator.getElevatorId() == 1);
        check("Initial floor is 0", elevator.getCurrentFloor() == 0);
        check("Initial state is IdleState", elevator.getState() instanceof IdleState);
        check("Display is not null", elevator.getDisplay() != null);
        check("InnerPanel is not null", elevator.getInnerPanel() != null);
        check("Door is not null", elevator.getDoor() != null);
        check("WeightSensor is not null", elevator.getWeightSensor() != null);
        check("DoorSensor is not null", elevator.getDoorSensor() != null);
        check("InnerPanel has 10 floor buttons (default)", elevator.getInnerPanel().getFloorButtons().size() == 10);
    }

    // ─────────────────────────────────────────────────────────
    // 4. State Transitions (Idle → Active → Idle → Waiting)
    // ─────────────────────────────────────────────────────────

    private static void testStateTransitions() {
        section("4. State Transitions");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        // Idle initially
        check("Starts in IdleState", elevator.getState() instanceof IdleState);

        // Idle → Active (via moveTo)
        elevator.getState().moveTo(5);
        // After moveTo from IdleState, it sets ActiveState, then ActiveState.moveTo sets IdleState
        check("After moveTo(5): floor is 5", elevator.getCurrentFloor() == 5);

        // Open door → WaitingState
        elevator.getState().openDoor();
        check("After openDoor: WaitingState", elevator.getState() instanceof WaitingState);

        // Close door from Waiting
        elevator.getState().closeDoor();
        // WaitingState.closeDoor just prints, stays in WaitingState
        check("After closeDoor from Waiting: still WaitingState", elevator.getState() instanceof WaitingState);

        // Move from Waiting → Active → Idle
        elevator.getState().moveTo(3);
        check("After moveTo(3) from Waiting: floor is 3", elevator.getCurrentFloor() == 3);
    }

    // ─────────────────────────────────────────────────────────
    // 5. Door Operations
    // ─────────────────────────────────────────────────────────

    private static void testDoorOperations() {
        section("5. Door Operations");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        Door door = elevator.getDoor();
        check("Door starts closed", !door.isOpen());

        elevator.openDoors();
        check("Door is open after openDoors()", door.isOpen());

        elevator.openDoors(); // idempotent
        check("Door stays open on double open", door.isOpen());

        elevator.closeDoors();
        check("Door is closed after closeDoors()", !door.isOpen());

        elevator.closeDoors(); // idempotent
        check("Door stays closed on double close", !door.isOpen());
    }

    // ─────────────────────────────────────────────────────────
    // 6. Sensor Events
    // ─────────────────────────────────────────────────────────

    private static void testSensorEvents() {
        section("6. Sensor Events (Door Obstruction)");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        check("Starts in IdleState", elevator.getState() instanceof IdleState);

        // Simulate door obstruction
        elevator.getDoorSensor().detectObstruction();
        check("After door obstruction: WaitingState", elevator.getState() instanceof WaitingState);
        check("DoorSensor reports obstructed", elevator.getDoorSensor().isObstructed());

        elevator.getDoorSensor().clearObstruction();
        check("DoorSensor reports clear", !elevator.getDoorSensor().isObstructed());
    }

    // ─────────────────────────────────────────────────────────
    // 7. Overweight State
    // ─────────────────────────────────────────────────────────

    private static void testOverweightState() {
        section("7. Overweight State");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        // Trigger overweight via weight sensor
        elevator.getWeightSensor().onWeightChange(800); // max is 750
        check("After overweight: OverweightState", elevator.getState() instanceof OverweightState);

        // Overweight state should refuse movement
        int floorBefore = elevator.getCurrentFloor();
        elevator.getState().moveTo(5);
        check("Cannot move when overweight (floor unchanged)", elevator.getCurrentFloor() == floorBefore);

        // Overweight state should refuse closing door
        elevator.getState().closeDoor(); // should print warning
        check("Still in OverweightState after closeDoor attempt", elevator.getState() instanceof OverweightState);
    }

    // ─────────────────────────────────────────────────────────
    // 8. Maintenance State
    // ─────────────────────────────────────────────────────────

    private static void testMaintenanceState() {
        section("8. Maintenance State");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        elevator.setState(new MaintenanceState(elevator));
        check("State is MaintenanceState", elevator.getState() instanceof MaintenanceState);

        int floorBefore = elevator.getCurrentFloor();
        elevator.getState().moveTo(7);
        check("Cannot move in maintenance (floor unchanged)", elevator.getCurrentFloor() == floorBefore);

        elevator.getState().handleRequest();
        check("Still in MaintenanceState after handleRequest", elevator.getState() instanceof MaintenanceState);
    }

    // ─────────────────────────────────────────────────────────
    // 9. Nearest Elevator Strategy
    // ─────────────────────────────────────────────────────────

    private static void testNearestElevatorStrategy() {
        section("9. Nearest Elevator Strategy");
        resetOrchestrator();

        Elevator e1 = new Elevator(1);
        Elevator e2 = new Elevator(2);
        Elevator e3 = new Elevator(3);

        // Position them at different floors
        e1.setCurrentFloor(0);
        e2.setCurrentFloor(5);
        e3.setCurrentFloor(9);

        List<Elevator> elevators = new ArrayList<>();
        elevators.add(e1);
        elevators.add(e2);
        elevators.add(e3);

        NearestElevatorStrategy strategy = new NearestElevatorStrategy();

        ElevatorRequest req1 = new ElevatorRequest(4, Direction.UP, System.currentTimeMillis());
        Elevator allocated1 = strategy.allocate(elevators, req1);
        check("Floor 4 request → Elevator 2 (at floor 5)", allocated1.getElevatorId() == 2);

        ElevatorRequest req2 = new ElevatorRequest(1, Direction.UP, System.currentTimeMillis());
        Elevator allocated2 = strategy.allocate(elevators, req2);
        check("Floor 1 request → Elevator 1 (at floor 0)", allocated2.getElevatorId() == 1);

        ElevatorRequest req3 = new ElevatorRequest(8, Direction.DOWN, System.currentTimeMillis());
        Elevator allocated3 = strategy.allocate(elevators, req3);
        check("Floor 8 request → Elevator 3 (at floor 9)", allocated3.getElevatorId() == 3);

        // Put e3 in maintenance, should be skipped
        e3.setState(new MaintenanceState(e3));
        ElevatorRequest req4 = new ElevatorRequest(8, Direction.DOWN, System.currentTimeMillis());
        Elevator allocated4 = strategy.allocate(elevators, req4);
        check("Floor 8 with E3 in maintenance → Elevator 2", allocated4.getElevatorId() == 2);
    }

    // ─────────────────────────────────────────────────────────
    // 10. Orchestrator Request Flow (end-to-end)
    // ─────────────────────────────────────────────────────────

    private static void testOrchestratorRequestFlow() {
        section("10. Orchestrator Request Flow");
        resetOrchestrator();

        ElevatorOrchestrator orch = ElevatorOrchestrator.getInstance();
        Elevator e1 = new Elevator(1);
        Elevator e2 = new Elevator(2);
        e1.setCurrentFloor(0);
        e2.setCurrentFloor(7);
        orch.addElevator(e1);
        orch.addElevator(e2);

        Floor floor3 = new Floor(3);
        orch.handleRequest(floor3, Direction.UP);

        check("After request to floor 3: E1 moved (nearest from 0)", e1.getCurrentFloor() == 3);
        check("E2 remains at floor 7", e2.getCurrentFloor() == 7);
    }

    // ─────────────────────────────────────────────────────────
    // 11. Floor & Outer Panel
    // ─────────────────────────────────────────────────────────

    private static void testFloorAndOuterPanel() {
        section("11. Floor & Outer Panel");
        resetOrchestrator();

        Floor floor = new Floor(5);
        check("Floor ID is 5", floor.getFloorId() == 5);
        check("OuterPanel is not null", floor.getOuterPanel() != null);
        check("OuterPanel up button exists", floor.getOuterPanel().getUpButton() != null);
        check("OuterPanel down button exists", floor.getOuterPanel().getDownButton() != null);

        // Test that pressing outer panel triggers orchestrator
        ElevatorOrchestrator orch = ElevatorOrchestrator.getInstance();
        Elevator e1 = new Elevator(1);
        orch.addElevator(e1);

        floor.getOuterPanel().pressUp();
        check("After pressUp on floor 5: E1 moved to 5", e1.getCurrentFloor() == 5);
    }

    // ─────────────────────────────────────────────────────────
    // 12. Inner Panel Buttons
    // ─────────────────────────────────────────────────────────

    private static void testInnerPanelButtons() {
        section("12. Inner Panel Buttons");
        resetOrchestrator();

        Elevator elevator = new Elevator(1, 5); // 5 floors
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        InnerPanel panel = elevator.getInnerPanel();
        check("InnerPanel has 5 floor buttons", panel.getFloorButtons().size() == 5);
        check("OpenButton exists", panel.getOpenButton() != null);
        check("CloseButton exists", panel.getCloseButton() != null);
        check("AlarmButton exists", panel.getAlarmButton() != null);

        // Press floor button 3
        panel.getFloorButtons().get(3).onPress();
        check("After pressing floor 3 button: moved to floor 3", elevator.getCurrentFloor() == 3);

        // Press open button
        panel.getOpenButton().onPress();
        check("After open button: door is open", elevator.getDoor().isOpen());

        // Press close button
        panel.getCloseButton().onPress();
        check("After close button: door is closed", !elevator.getDoor().isOpen());
    }

    // ─────────────────────────────────────────────────────────
    // 13. Display Observer
    // ─────────────────────────────────────────────────────────

    private static void testDisplayObserver() {
        section("13. Display Observer");
        resetOrchestrator();

        Elevator elevator = new Elevator(1);
        ElevatorOrchestrator.getInstance().addElevator(elevator);

        check("Display is not null", elevator.getDisplay() != null);
        // State changes should trigger display update via orchestrator
        // (display.update is called inside notifyDisplay — we verify no exception occurs)
        elevator.setState(new ActiveState(elevator));
        check("State change triggers display update (no exception)", true);
        elevator.setState(new IdleState(elevator));
        check("Another state change (no exception)", true);
    }

    // ─────────────────────────────────────────────────────────
    // 14. Multiple Elevators
    // ─────────────────────────────────────────────────────────

    private static void testMultipleElevators() {
        section("14. Multiple Elevators");
        resetOrchestrator();

        ElevatorOrchestrator orch = ElevatorOrchestrator.getInstance();
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Elevator e = new Elevator(i);
            e.setCurrentFloor(i * 2); // 0, 2, 4, 6, 8, 10
            orch.addElevator(e);
            elevators.add(e);
        }
        check("Orchestrator has 6 elevators", orch.getElevators().size() == 6);

        // Request at floor 5 → closest is Elevator 2 (at floor 4) or Elevator 3 (at floor 6)
        Floor floor5 = new Floor(5);
        orch.handleRequest(floor5, Direction.UP);
        boolean eitherMoved = elevators.get(2).getCurrentFloor() == 5 || elevators.get(3).getCurrentFloor() == 5;
        check("Floor 5 request allocated to nearest elevator", eitherMoved);
    }

    // ─────────────────────────────────────────────────────────
    // 15. Concurrent Requests (Multithreaded Simulation)
    // ─────────────────────────────────────────────────────────

    private static void testConcurrentRequests() throws Exception {
        section("15. Concurrent Requests (Multithreaded)");
        resetOrchestrator();

        ElevatorOrchestrator orch = ElevatorOrchestrator.getInstance();
        for (int i = 0; i < 4; i++) {
            Elevator e = new Elevator(i);
            e.setCurrentFloor(i * 3); // 0, 3, 6, 9
            orch.addElevator(e);
        }

        int requestCount = 20;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(requestCount);
        AtomicInteger completedRequests = new AtomicInteger(0);
        AtomicInteger errors = new AtomicInteger(0);

        for (int i = 0; i < requestCount; i++) {
            int floorNo = i % 10;
            Direction dir = (i % 2 == 0) ? Direction.UP : Direction.DOWN;
            pool.submit(() -> {
                try {
                    startLatch.await();
                    Floor floor = new Floor(floorNo);
                    orch.handleRequest(floor, dir);
                    completedRequests.incrementAndGet();
                } catch (Exception ex) {
                    errors.incrementAndGet();
                    ex.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // fire all at once
        boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
        pool.shutdown();

        check("All " + requestCount + " concurrent requests completed", finished);
        check("No exceptions during concurrent execution", errors.get() == 0);
        check("All requests processed", completedRequests.get() == requestCount);
    }

    // ─────────────────────────────────────────────────────────
    // 16. Concurrent Elevator Movement (Multithreaded)
    // ─────────────────────────────────────────────────────────

    private static void testConcurrentElevatorMovement() throws Exception {
        section("16. Concurrent Elevator Movement (Multithreaded)");
        resetOrchestrator();

        int elevatorCount = 8;
        List<Elevator> elevators = new ArrayList<>();
        ElevatorOrchestrator orch = ElevatorOrchestrator.getInstance();

        for (int i = 0; i < elevatorCount; i++) {
            Elevator e = new Elevator(i);
            orch.addElevator(e);
            elevators.add(e);
        }

        ExecutorService pool = Executors.newFixedThreadPool(elevatorCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(elevatorCount);
        AtomicInteger errors = new AtomicInteger(0);

        // Each elevator simultaneously moves through a sequence of floors
        for (int i = 0; i < elevatorCount; i++) {
            Elevator elevator = elevators.get(i);
            int targetFloor = (i + 1) * 2; // 2, 4, 6, 8, 10, 12, 14, 16
            pool.submit(() -> {
                try {
                    startLatch.await();
                    elevator.moveTo(targetFloor);
                    // Simulate open/close cycle
                    elevator.openDoors();
                    Thread.sleep(50);
                    elevator.closeDoors();
                } catch (Exception ex) {
                    errors.incrementAndGet();
                    ex.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
        pool.shutdown();

        check("All " + elevatorCount + " elevators finished movement", finished);
        check("No exceptions during concurrent movement", errors.get() == 0);

        // Verify each elevator reached its target
        for (int i = 0; i < elevatorCount; i++) {
            int expected = (i + 1) * 2;
            check("Elevator " + i + " at floor " + expected, elevators.get(i).getCurrentFloor() == expected);
        }
    }

    // ─────────────────────────────────────────────────────────
    // 17. Strategy Swap at Runtime
    // ─────────────────────────────────────────────────────────

    private static void testStrategySwap() {
        section("17. Strategy Swap at Runtime");
        resetOrchestrator();

        ElevatorOrchestrator orch = ElevatorOrchestrator.getInstance();
        Elevator e1 = new Elevator(1);
        Elevator e2 = new Elevator(2);
        e1.setCurrentFloor(0);
        e2.setCurrentFloor(9);
        orch.addElevator(e1);
        orch.addElevator(e2);

        // Default is NearestElevatorStrategy
        Floor floor8 = new Floor(8);
        orch.handleRequest(floor8, Direction.DOWN);
        check("Default strategy: E2 (nearest to 8) moved", e2.getCurrentFloor() == 8);

        // Swap to a custom "always pick first" strategy
        orch.setStrategy((elevators, req) -> elevators.isEmpty() ? null : elevators.get(0));

        Floor floor7 = new Floor(7);
        orch.handleRequest(floor7, Direction.UP);
        check("Custom strategy: E1 (first) moved to 7", e1.getCurrentFloor() == 7);
    }
}
