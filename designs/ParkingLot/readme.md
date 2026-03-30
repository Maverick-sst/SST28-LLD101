# Parking Lot System — Low Level Design

A production-grade parking lot management system implementing **Step Builder**, **Strategy**, **Singleton**, and **synchronized slot allocation** patterns.

## Architecture Overview

Multi-floor parking lot with distance-based slot allocation (priority queues), hourly billing, and thread-safe concurrent operations. Each floor has a grid of parking slots auto-initialized via a distribution strategy, with entry/exit gates positioned for Euclidean distance calculation.

## UML Class Diagram

```mermaid
classDiagram
    direction TB

    %% ─── Enums ───
    class SlotType {
        <<enumeration>>
        SMALL : 25.0/hr
        MEDIUM : 50.0/hr
        LARGE : 75.0/hr
        -double hourlyRate
        +getHourlyRate() double
    }

    %% ─── Core Entities ───
    class Vehicle {
        -String brand
        -String model
        -String numberPlate
        -String color
        +Vehicle(brand, model, numberPlate, color)
    }

    class ParkingSlot {
        -String id
        -int row
        -int col
        -SlotType slotType
        -String floorId
        -boolean isOccupied
        -double distanceFromGate
        +equals(Object o) boolean
        +hashCode() int
        +setOccupied(boolean)
    }

    class ParkingTicket {
        -String ticketId
        -Vehicle vehicle
        -ParkingSlot assignedSlot
        -String entryGateId
        -LocalDateTime inTime
    }

    class ParkingBill {
        -ParkingTicket ticket
        -LocalDateTime outTime
        -double totalCost
    }

    %% ─── Gates ───
    class EntryGate {
        -int row
        -int col
        -String gateId
    }

    class ExitGate {
        -int row
        -int col
        -String gateId
    }

    %% ─── Strategy Interfaces ───
    class SlotAllocationStrategy {
        <<interface>>
        +getNearestSlot(SlotType) ParkingSlot
    }

    class SlotDistributionStrategy {
        <<interface>>
        +getDistribution() Map~SlotType, Integer~
    }

    class MySlotAllocationStrategy {
        -SlotPool pool
        +getNearestSlot(SlotType) ParkingSlot
    }

    class MySlotDistStrategy {
        +getDistribution() Map~SlotType, Integer~
    }

    SlotAllocationStrategy <|.. MySlotAllocationStrategy
    SlotDistributionStrategy <|.. MySlotDistStrategy

    %% ─── Pool & Initialization ───
    class SlotPool {
        -Map~SlotType, PriorityQueue~ slotMap
        +addParkingSlot(SlotType, ParkingSlot)
        +allocateSlot(SlotType) ParkingSlot
        +releaseSlot(ParkingSlot)
        +getAvailableCountByType(SlotType) int
    }

    class SlotInitializer {
        -int rows
        -int cols
        -SlotDistributionStrategy strategy
        -SlotPool pool
        -EntryGate entryGate
        -String floorId
        +initialize() ParkingSlot[][]
    }

    %% ─── Floor (Step Builder) ───
    class Floor {
        -String floorId
        -int rows
        -int cols
        -ParkingSlot[][] grid
        -EntryGate entryGate
        -ExitGate exitGate
        -SlotPool pool
        -SlotAllocationStrategy allocationStrategy
        +builder() floorStep$
        +getNearestSlot(SlotType) ParkingSlot
        +releaseSlot(ParkingSlot)
        +getAvailableCountByType(SlotType) int
    }

    class Builder {
        <<inner class>>
        implements floorStep, RowStep,
        ColStep, EntryGateStep,
        ExitGateStep, SlotPoolStep,
        DistributionStep, GridStep,
        AllocationStep, BuildStep
    }

    %% ─── Services ───
    class TicketService {
        -TicketStore ticketStore
        +generateTicket(...) ParkingTicket
        +getTicket(String) ParkingTicket
    }

    class TicketStore {
        -HashMap~String, ParkingTicket~ ticketStore
        +setTicket(String, ParkingTicket)
        +getTicket(String) ParkingTicket
    }

    class BillingService {
        -Map~String, Floor~ floorMap
        +generateBill(ParkingTicket, LocalDateTime) ParkingBill
    }

    class SlotsStatusService {
        -Map~String, Floor~ floorMap
        +getStatus() SlotStatusResponse
    }

    class SlotStatusResponse {
        -Map~String, Map~ slotStatusMap
        +getSlotsStatus() Map
    }

    %% ─── Singleton Façade ───
    class ParkingLot {
        <<Singleton>>
        -static volatile ParkingLot parkingLot
        -LinkedHashMap~String, Floor~ floorMap
        -TicketService ticketService
        -BillingService billingService
        -SlotsStatusService slotsStatusService
        +getInstance() ParkingLot$
        +addFloor(Floor)
        +getAvailableEntryGate(SlotType) String
        +generateTicket(...) ParkingTicket
        +generateBill(String, LocalDateTime) ParkingBill
        +showStatus() SlotStatusResponse
    }

    %% ─── Custom Exceptions ───
    class NoSlotAvailableException {
        <<exception>>
    }
    class ParkingFullException {
        <<exception>>
    }
    class TicketNotFoundException {
        <<exception>>
    }

    %% ─── Relationships ───
    ParkingLot "1" *-- "*" Floor : manages
    ParkingLot *-- TicketService : has
    ParkingLot *-- BillingService : has
    ParkingLot *-- SlotsStatusService : has

    Floor *-- EntryGate : has
    Floor *-- ExitGate : has
    Floor *-- SlotPool : has
    Floor --> SlotAllocationStrategy : uses
    Floor o-- Builder : built by

    SlotPool o-- ParkingSlot : stores in PriorityQueue
    SlotInitializer --> SlotPool : populates
    SlotInitializer --> SlotDistributionStrategy : reads distribution
    SlotInitializer --> EntryGate : calculates distance from

    MySlotAllocationStrategy --> SlotPool : delegates to

    TicketService --> TicketStore : persists in
    BillingService --> Floor : releases slot via

    ParkingTicket --> Vehicle : references
    ParkingTicket --> ParkingSlot : references
    ParkingBill --> ParkingTicket : wraps
    ParkingSlot --> SlotType : typed by
```

## Design Patterns Used

| Pattern | Where | Purpose |
|---------|-------|---------|
| **Singleton** | `ParkingLot` | Single façade with double-check locking (`volatile` + `synchronized`) |
| **Step Builder** | `Floor.Builder` | Enforces correct build order via chained interfaces (11 steps) |
| **Strategy** | `SlotAllocationStrategy`, `SlotDistributionStrategy` | Swappable allocation logic and slot-type distribution ratios |
| **Façade** | `ParkingLot` | Orchestrates `TicketService`, `BillingService`, `SlotsStatusService` |

## Slot Allocation Flow

```mermaid
sequenceDiagram
    participant User
    participant ParkingLot
    participant Floor
    participant SlotPool
    participant TicketService
    participant BillingService

    User->>ParkingLot: getAvailableEntryGate(SlotType)
    ParkingLot->>Floor: getAvailableCountByType(type)
    Floor-->>ParkingLot: count > 0 → gateId

    User->>ParkingLot: generateTicket(vehicle, type, gateId, inTime)
    ParkingLot->>Floor: getNearestSlot(type)
    Floor->>SlotPool: allocateSlot(type)
    Note over SlotPool: synchronized per SlotType<br/>PriorityQueue by distance
    SlotPool-->>Floor: nearest ParkingSlot
    ParkingLot->>TicketService: generateTicket(...)
    TicketService-->>User: ParkingTicket

    User->>ParkingLot: generateBill(ticketId, outTime)
    ParkingLot->>BillingService: generateBill(ticket, outTime)
    Note over BillingService: cost = hours × hourlyRate
    BillingService->>Floor: releaseSlot(slot)
    Floor->>SlotPool: releaseSlot(slot)
    BillingService-->>User: ParkingBill
```

## Concurrency Design

- **`SlotPool.allocateSlot()`** — `synchronized` on per-type `PriorityQueue`, ensures no two threads get the same slot
- **`SlotPool.releaseSlot()`** — `synchronized` on the same queue, safely re-adds released slots
- **`ParkingLot`** — Singleton with `volatile` + DCL, thread-safe initialization

## Test Suite Structure — `ParkingLotTest.java`

The existing test file contains **13 test groups** covering all parameters:

| # | Test | What it validates |
|---|------|-------------------|
| 1 | **Singleton** | `getInstance()` returns same object |
| 2 | **Floor Setup & Status** | Multi-floor setup, correct slot counts per type |
| 3 | **Entry Gate Lookup** | Returns first available floor's gate |
| 4 | **Ticket Generation** | Vehicle details, slot assignment, type matching |
| 5 | **Nearest Slot** | Priority queue returns slot closest to entry gate |
| 6 | **Count Decrement** | Available count drops by 1 after allocation |
| 7 | **Bill Generation** | Cost = hours × rate, slot released back to pool |
| 8 | **ParkingFullException** | Thrown when all slots of a type are taken |
| 9 | **Floor Fallback** | Falls back to next floor when current floor is full |
| 10 | **Slot Reuse** | Released slot re-enters pool and is reassigned |
| 11 | **🔥 Concurrent — 40 threads** | 40 threads race for 40 SMALL slots, no duplicates |
| 12 | **🔥 Concurrent — Overflow** | 25 threads for 20 slots: 20 succeed, 5 throw `NoSlotAvailableException` |
| 13 | **🔥 Concurrent — Alloc+Release** | 20 release threads + 20 allocation threads run simultaneously |

### Running Tests

```bash
cd ~/LLD
javac designs/ParkingLot/*.java
java designs.ParkingLot.ParkingLotTest
```

## File Structure

```
designs/ParkingLot/
├── ParkingLot.java              # Singleton façade
├── Floor.java                   # Step Builder with 11 chained interfaces
├── ParkingSlot.java             # Grid-positioned slot with distance
├── SlotPool.java                # Synchronized PriorityQueue allocation
├── SlotInitializer.java         # Grid builder using distribution strategy
├── SlotType.java                # SMALL / MEDIUM / LARGE enum with rates
├── Vehicle.java                 # Immutable vehicle entity
├── ParkingTicket.java           # Ticket with vehicle + slot + time
├── ParkingBill.java             # Bill with cost calculation
├── EntryGate.java               # Gate position for distance calc
├── ExitGate.java                # Exit gate position
├── SlotAllocationStrategy.java  # Strategy interface
├── SlotDistributionStrategy.java# Distribution strategy interface
├── MySlotAllocationStrategy.java# Nearest-slot implementation
├── MySlotDistStrategy.java      # 40/40/20 distribution
├── TicketService.java           # Ticket generation + lookup
├── TicketStore.java             # In-memory ticket storage
├── BillingService.java          # Bill calculation + slot release
├── SlotsStatusService.java      # Floor-wise availability aggregation
├── SlotStatusResponse.java      # Status response DTO
├── NoSlotAvailableException.java
├── ParkingFullException.java
├── TicketNotFoundException.java
└── ParkingLotTest.java          # 13-test comprehensive suite
```
