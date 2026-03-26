package designs.ParkingLot;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLotTest {

    // ─── ANSI colors for output ───────────────────────────────────────────────
    static final String GREEN  = "\u001B[32m";
    static final String RED    = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";

    static int passed = 0;
    static int failed = 0;

    static void pass(String testName) {
        System.out.println(GREEN + "  ✓ PASS: " + testName + RESET);
        passed++;
    }

    static void fail(String testName, String reason) {
        System.out.println(RED + "  ✗ FAIL: " + testName + " → " + reason + RESET);
        failed++;
    }

    // ─── Floor factory helper ─────────────────────────────────────────────────
    // 10x10 grid, entry gate at (0,0), distribution: 40 SMALL / 40 MEDIUM / 20 LARGE
    static Floor buildFloor(String floorId) {
        SlotPool pool = new SlotPool();
        EntryGate entry = new EntryGate(0, 0, floorId);
        ExitGate  exit  = new ExitGate(9, 9, floorId + "-EXIT");
        SlotDistributionStrategy dist = new MySlotDistStrategy();
        SlotAllocationStrategy   alloc = new MySlotAllocationStrategy(pool);

        return Floor.builder()
                .withFloor(floorId)
                .withRow(10)
                .withCol(10)
                .withEntryGate(entry)
                .withExitGate(exit)
                .withSlotPool(pool)
                .withDistribution(dist)
                .withGrid()
                .withAllocation(alloc)
                .build();
    }

    // ─── Fresh ParkingLot per test via reflection reset ───────────────────────
    // Singleton needs resetting between tests to avoid state bleed
    static void resetSingleton() {
        try {
            var field = ParkingLot.class.getDeclaredField("parkingLot");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            System.out.println(RED + "Could not reset singleton: " + e.getMessage() + RESET);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 1 — Singleton: same instance across multiple calls
    // ═════════════════════════════════════════════════════════════════════════
    static void testSingleton() {
        System.out.println(BOLD + "\n[TEST 1] Singleton" + RESET);
        resetSingleton();
        ParkingLot a = ParkingLot.getInstance();
        ParkingLot b = ParkingLot.getInstance();
        if (a == b) pass("Same instance returned by getInstance()");
        else        fail("Same instance returned by getInstance()", "Two different objects returned");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 2 — Floor setup and showStatus
    // ═════════════════════════════════════════════════════════════════════════
    static void testFloorSetupAndStatus() {
        System.out.println(BOLD + "\n[TEST 2] Floor Setup & showStatus" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));
        lot.addFloor(buildFloor("F1"));

        SlotStatusResponse status = lot.showStatus();
        Map<String, Map<SlotType, Integer>> map = status.getSlotsStatus();

        if (map.containsKey("F0") && map.containsKey("F1"))
            pass("showStatus contains both floors");
        else
            fail("showStatus contains both floors", "Missing floors: " + map.keySet());

        int smallF0 = map.get("F0").get(SlotType.SMALL);
        if (smallF0 == 40) pass("F0 SMALL count = 40");
        else               fail("F0 SMALL count = 40", "Got " + smallF0);

        int largeF0 = map.get("F0").get(SlotType.LARGE);
        if (largeF0 == 20) pass("F0 LARGE count = 20");
        else               fail("F0 LARGE count = 20", "Got " + largeF0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 3 — getAvailableEntryGate returns first floor with space
    // ═════════════════════════════════════════════════════════════════════════
    static void testGetAvailableEntryGate() {
        System.out.println(BOLD + "\n[TEST 3] getAvailableEntryGate" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));
        lot.addFloor(buildFloor("F1"));

        try {
            String gate = lot.getAvailableEntryGate(SlotType.SMALL);
            if ("F0".equals(gate)) pass("Returns F0 when both floors available");
            else                   fail("Returns F0 when both floors available", "Got " + gate);
        } catch (Exception e) {
            fail("Returns F0 when both floors available", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 4 — generateTicket returns valid ticket with correct fields
    // ═════════════════════════════════════════════════════════════════════════
    static void testGenerateTicket() {
        System.out.println(BOLD + "\n[TEST 4] generateTicket basic" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));

        Vehicle v = new Vehicle("Toyota", "Innova", "MH01AB1234", "White");
        LocalDateTime inTime = LocalDateTime.now();

        try {
            ParkingTicket ticket = lot.generateTicket(v, SlotType.SMALL, "F0", inTime);

            if (ticket != null)
                pass("Ticket is not null");
            else {
                fail("Ticket is not null", "null returned");
                return;
            }

            if (ticket.getVehicle().getNumberPlate().equals("MH01AB1234"))
                pass("Vehicle details preserved in ticket");
            else
                fail("Vehicle details preserved in ticket", "Wrong number plate");

            if (ticket.getAssignedSlot() != null)
                pass("Slot assigned in ticket");
            else
                fail("Slot assigned in ticket", "assignedSlot is null");

            if (SlotType.SMALL == ticket.getAssignedSlot().getSlotType())
                pass("Assigned slot type matches requested type");
            else
                fail("Assigned slot type matches requested type",
                        "Got " + ticket.getAssignedSlot().getSlotType());

            if ("F0".equals(ticket.getAssignedSlot().getFloorId()))
                pass("Assigned slot belongs to correct floor");
            else
                fail("Assigned slot belongs to correct floor",
                        "Got floorId " + ticket.getAssignedSlot().getFloorId());

        } catch (Exception e) {
            fail("generateTicket should not throw", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 5 — Nearest slot: slot at (0,0) is closest to gate at (0,0)
    // ═════════════════════════════════════════════════════════════════════════
    static void testNearestSlotIsClosest() {
        System.out.println(BOLD + "\n[TEST 5] Nearest slot allocation" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));

        Vehicle v = new Vehicle("Honda", "City", "MH02XY5678", "Black");
        try {
            ParkingTicket ticket = lot.generateTicket(v, SlotType.SMALL, "F0", LocalDateTime.now());
            ParkingSlot slot = ticket.getAssignedSlot();

            // Entry gate is at (0,0). SMALL zone starts at index 0 → first slot is (0,0)
            if (slot.getRow() == 0 && slot.getCol() == 0)
                pass("First allocated SMALL slot is (0,0) — nearest to gate");
            else
                fail("First allocated SMALL slot is (0,0)",
                        "Got (" + slot.getRow() + "," + slot.getCol() + ")");

        } catch (Exception e) {
            fail("Nearest slot test", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 6 — Available count decrements after ticket generation
    // ═════════════════════════════════════════════════════════════════════════
    static void testAvailableCountDecrement() {
        System.out.println(BOLD + "\n[TEST 6] Available count decrements on allocation" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));

        SlotStatusResponse before = lot.showStatus();
        int beforeCount = before.getSlotsStatus().get("F0").get(SlotType.MEDIUM);

        try {
            lot.generateTicket(new Vehicle("BMW", "3 Series", "DL01ZZ9999", "Blue"),
                    SlotType.MEDIUM, "F0", LocalDateTime.now());

            SlotStatusResponse after = lot.showStatus();
            int afterCount = after.getSlotsStatus().get("F0").get(SlotType.MEDIUM);

            if (afterCount == beforeCount - 1)
                pass("MEDIUM count decremented from " + beforeCount + " to " + afterCount);
            else
                fail("MEDIUM count should decrement by 1",
                        "Before: " + beforeCount + " After: " + afterCount);

        } catch (Exception e) {
            fail("Available count decrement", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 7 — generateBill: cost calculation and slot release
    // ═════════════════════════════════════════════════════════════════════════
    static void testGenerateBill() {
        System.out.println(BOLD + "\n[TEST 7] generateBill — cost + slot release" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));

        Vehicle v = new Vehicle("Maruti", "Swift", "KA03AB0001", "Red");
        LocalDateTime inTime  = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime outTime = LocalDateTime.of(2025, 1, 1, 12, 30); // 2.5 hours

        try {
            ParkingTicket ticket = lot.generateTicket(v, SlotType.LARGE, "F0", inTime);
            String ticketId = ticket.getTicketId();

            int beforeRelease = lot.showStatus().getSlotsStatus().get("F0").get(SlotType.LARGE);

            ParkingBill bill = lot.generateBill(ticketId, outTime);

            // LARGE = 75.0/hr, 2.5 hrs = 187.5
            double expectedCost = 2.5 * 75.0;
            if (Math.abs(bill.getTotalCost() - expectedCost) < 0.01)
                pass("Bill cost correct: ₹" + bill.getTotalCost());
            else
                fail("Bill cost", "Expected ₹" + expectedCost + " got ₹" + bill.getTotalCost());

            int afterRelease = lot.showStatus().getSlotsStatus().get("F0").get(SlotType.LARGE);
            if (afterRelease == beforeRelease + 1)
                pass("Slot released back to pool after billing");
            else
                fail("Slot released after billing",
                        "Before: " + beforeRelease + " After: " + afterRelease);

        } catch (Exception e) {
            fail("generateBill", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 8 — ParkingFullException when all slots of a type are taken
    // ═════════════════════════════════════════════════════════════════════════
    static void testParkingFullException() {
        System.out.println(BOLD + "\n[TEST 8] ParkingFullException when lot is full" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0")); // 20 LARGE slots

        // Fill all 20 LARGE slots
        try {
            for (int i = 0; i < 20; i++) {
                lot.generateTicket(
                        new Vehicle("Volvo", "Bus", "MH04BUS" + String.format("%04d", i), "Orange"),
                        SlotType.LARGE, "F0", LocalDateTime.now());
            }
        } catch (Exception e) {
            fail("Filling 20 LARGE slots should not throw", e.getMessage());
            return;
        }

        // Now getAvailableEntryGate should throw ParkingFullException
        try {
            lot.getAvailableEntryGate(SlotType.LARGE);
            fail("Should throw ParkingFullException when LARGE is full", "No exception thrown");
        } catch (ParkingFullException e) {
            pass("ParkingFullException thrown when all LARGE slots occupied");
        } catch (Exception e) {
            fail("ParkingFullException expected", "Got: " + e.getClass().getSimpleName());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 9 — Fallback to next floor when first floor is full
    // ═════════════════════════════════════════════════════════════════════════
    static void testFallbackToNextFloor() {
        System.out.println(BOLD + "\n[TEST 9] getAvailableEntryGate fallback to F1 when F0 full" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0")); // 20 LARGE
        lot.addFloor(buildFloor("F1")); // 20 LARGE

        // Fill all LARGE on F0
        try {
            for (int i = 0; i < 20; i++) {
                lot.generateTicket(
                        new Vehicle("Volvo", "Bus", "MH05BUS" + String.format("%04d", i), "Yellow"),
                        SlotType.LARGE, "F0", LocalDateTime.now());
            }
            String gate = lot.getAvailableEntryGate(SlotType.LARGE);
            if ("F1".equals(gate))
                pass("Correctly falls back to F1 when F0 LARGE is full");
            else
                fail("Fallback to F1", "Got gate: " + gate);
        } catch (Exception e) {
            fail("Fallback to next floor", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 10 — Slot reuse: released slot reappears in pool
    // ═════════════════════════════════════════════════════════════════════════
    static void testSlotReuse() {
        System.out.println(BOLD + "\n[TEST 10] Slot reuse after release" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0"));

        LocalDateTime inTime  = LocalDateTime.of(2025, 3, 1, 8, 0);
        LocalDateTime outTime = LocalDateTime.of(2025, 3, 1, 9, 0);

        try {
            // Park and bill vehicle 1
            ParkingTicket t1 = lot.generateTicket(
                    new Vehicle("Bajaj", "Pulsar", "MH06PU0001", "Black"),
                    SlotType.SMALL, "F0", inTime);
            String slotIdFirst = t1.getAssignedSlot().getId();
            lot.generateBill(t1.getTicketId(), outTime);

            // Park vehicle 2 — should get same nearest slot
            ParkingTicket t2 = lot.generateTicket(
                    new Vehicle("Hero", "Splendor", "MH06SP0002", "Silver"),
                    SlotType.SMALL, "F0", inTime);
            String slotIdSecond = t2.getAssignedSlot().getId();

            if (slotIdFirst.equals(slotIdSecond))
                pass("Released slot reassigned to next vehicle — slot reuse works");
            else
                fail("Slot reuse", "First slot: " + slotIdFirst + " Second slot: " + slotIdSecond);

        } catch (Exception e) {
            fail("Slot reuse test", e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 11 — CONCURRENCY: 40 threads race for 40 SMALL slots simultaneously
    //           No two threads should get the same slot
    // ═════════════════════════════════════════════════════════════════════════
    static void testConcurrentAllocationNoDuplicates() throws InterruptedException {
        System.out.println(BOLD + "\n[TEST 11] CONCURRENCY — 40 threads race for 40 SMALL slots" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0")); // exactly 40 SMALL slots

        int threadCount = 40;
        CountDownLatch startLatch = new CountDownLatch(1); // all threads start simultaneously
        CountDownLatch doneLatch  = new CountDownLatch(threadCount);

        Set<ParkingSlot>      assignedSlots = Collections.synchronizedSet(new HashSet<>());
        List<String>          errors        = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger         successCount  = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    startLatch.await(); // all threads wait here until released together
                    Vehicle v = new Vehicle("Brand" + id, "Model" + id, "PLATE" + id, "Color" + id);
                    ParkingTicket ticket = lot.generateTicket(v, SlotType.SMALL, "F0", LocalDateTime.now());
                    ParkingSlot slot = ticket.getAssignedSlot();

                    boolean added = assignedSlots.add(slot);
                    if (!added) {
                        errors.add("DUPLICATE SLOT: " + slot.getId()
                                + " at (" + slot.getRow() + "," + slot.getCol() + ")");
                    } else {
                        successCount.incrementAndGet();
                    }
                } catch (NoSlotAvailableException e) {
                    errors.add("NoSlotAvailable for thread " + id);
                } catch (Exception e) {
                    errors.add("Thread " + id + " threw: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown(); // release all threads at once
        doneLatch.await(10, TimeUnit.SECONDS);

        if (errors.isEmpty()) {
            pass("All 40 threads got unique slots — no concurrency violation");
        } else {
            fail("Concurrent allocation produced violations", errors.size() + " error(s)");
            errors.forEach(e -> System.out.println(RED + "    → " + e + RESET));
        }

        if (successCount.get() == 40)
            pass("All 40 threads succeeded — no slot was missed");
        else
            fail("All 40 threads should succeed", "Only " + successCount.get() + " succeeded");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 12 — CONCURRENCY: more threads than slots — excess should throw
    // ═════════════════════════════════════════════════════════════════════════
    static void testConcurrentOverflowThrowsException() throws InterruptedException {
        System.out.println(BOLD + "\n[TEST 12] CONCURRENCY — 25 threads race for 20 LARGE slots" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0")); // 20 LARGE slots

        int threadCount = 25;
        CountDownLatch startLatch    = new CountDownLatch(1);
        CountDownLatch doneLatch     = new CountDownLatch(threadCount);
        AtomicInteger successCount   = new AtomicInteger(0);
        AtomicInteger exceptionCount = new AtomicInteger(0);
        Set<ParkingSlot> assignedSlots = Collections.synchronizedSet(new HashSet<>());
        List<String>     duplicates    = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    startLatch.await();
                    Vehicle v = new Vehicle("Bus" + id, "Heavy" + id, "BUSPLATE" + id, "Red");
                    ParkingTicket ticket = lot.generateTicket(v, SlotType.LARGE, "F0", LocalDateTime.now());
                    boolean added = assignedSlots.add(ticket.getAssignedSlot());
                    if (!added) duplicates.add("Duplicate: " + ticket.getAssignedSlot().getId());
                    else successCount.incrementAndGet();
                } catch (NoSlotAvailableException e) {
                    exceptionCount.incrementAndGet();
                } catch (Exception e) {
                    duplicates.add("Unexpected: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        if (successCount.get() == 20)
            pass("Exactly 20 threads succeeded — matched slot count");
        else
            fail("Exactly 20 should succeed", "Got " + successCount.get());

        if (exceptionCount.get() == 5)
            pass("Exactly 5 threads got NoSlotAvailableException — overflow handled");
        else
            fail("Exactly 5 should get NoSlotAvailableException", "Got " + exceptionCount.get());

        if (duplicates.isEmpty())
            pass("Zero duplicate slots assigned under overflow — concurrency safe");
        else {
            fail("No duplicates under overflow", duplicates.size() + " duplicate(s)");
            duplicates.forEach(d -> System.out.println(RED + "    → " + d + RESET));
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST 13 — CONCURRENCY: simultaneous alloc + release stress test
    // ═════════════════════════════════════════════════════════════════════════
    static void testConcurrentAllocAndRelease() throws InterruptedException {
        System.out.println(BOLD + "\n[TEST 13] CONCURRENCY — simultaneous alloc + release stress" + RESET);
        resetSingleton();
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(buildFloor("F0")); // 40 MEDIUM slots

        // First park 20 vehicles
        List<ParkingTicket> parkedTickets = new ArrayList<>();
        try {
            for (int i = 0; i < 20; i++) {
                parkedTickets.add(lot.generateTicket(
                        new Vehicle("Car", "Sedan", "INIT" + i, "Grey"),
                        SlotType.MEDIUM, "F0",
                        LocalDateTime.of(2025, 1, 1, 8, 0)));
            }
        } catch (Exception e) {
            fail("Initial parking setup", e.getMessage());
            return;
        }

        int threadCount = 20;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(threadCount * 2);
        AtomicInteger  errors     = new AtomicInteger(0);

        // 20 threads release existing tickets simultaneously
        for (int i = 0; i < threadCount; i++) {
            final ParkingTicket ticket = parkedTickets.get(i);
            final int id = i;
            new Thread(() -> {
                try {
                    startLatch.await();
                    lot.generateBill(ticket.getTicketId(),
                            LocalDateTime.of(2025, 1, 1, 10, 0));
                } catch (Exception e) {
                    errors.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        // 20 threads allocate new slots simultaneously
        Set<ParkingSlot> newSlots = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    startLatch.await();
                    Thread.sleep(5); // slight delay so some releases happen first
                    ParkingTicket t = lot.generateTicket(
                            new Vehicle("New", "Car", "NEW" + id, "White"),
                            SlotType.MEDIUM, "F0", LocalDateTime.now());
                    newSlots.add(t.getAssignedSlot());
                } catch (Exception e) {
                    errors.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await(15, TimeUnit.SECONDS);

        if (errors.get() == 0)
            pass("Simultaneous alloc+release stress — zero errors");
        else
            fail("Simultaneous alloc+release stress", errors.get() + " error(s) occurred");

        if (newSlots.size() == 20)
            pass("All 20 new vehicles got unique slots during stress");
        else
            fail("All 20 new vehicles should get unique slots", "Got " + newSlots.size() + " unique");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // MAIN
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) throws InterruptedException {
        System.out.println(BOLD + "\n══════════════════════════════════════════");
        System.out.println("  PARKING LOT SYSTEM — FULL TEST SUITE");
        System.out.println("══════════════════════════════════════════" + RESET);

        testSingleton();
        testFloorSetupAndStatus();
        testGetAvailableEntryGate();
        testGenerateTicket();
        testNearestSlotIsClosest();
        testAvailableCountDecrement();
        testGenerateBill();
        testParkingFullException();
        testFallbackToNextFloor();
        testSlotReuse();
        testConcurrentAllocationNoDuplicates();
        testConcurrentOverflowThrowsException();
        testConcurrentAllocAndRelease();

        System.out.println(BOLD + "\n══════════════════════════════════════════");
        System.out.printf("  RESULTS: %s%d PASSED%s  |  %s%d FAILED%s%n",
                GREEN, passed, RESET, RED, failed, RESET);
        System.out.println("══════════════════════════════════════════" + RESET);
    }
}