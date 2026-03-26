package designs.ParkingLot;

public class Floor {
    private final String floorId;
    private final int rows;
    private final int cols;
    private final ParkingSlot[][] grid;
    private final EntryGate entryGate;
    private final ExitGate exitGate;

    private final SlotPool pool;
    // private final SlotInitializer slotInit;
    private final SlotAllocationStrategy allocationStrategy;

    // private final SlotDistributionStrategy distributionStrategy;
    public Floor(Builder builder) {
        // using builder to avoid fat constructor
        this.floorId = builder.floorId;
        this.rows = builder.rows;
        this.cols = builder.cols;
        this.grid = builder.grid;
        this.entryGate = builder.entryGate;
        this.exitGate = builder.exitGate;
        this.pool = builder.pool;
        this.allocationStrategy = builder.allocationStrategy;
    }

    public static floorStep builder() {
        return new Builder();
    }

    public interface floorStep {
        RowStep withFloor(String floorId);
    }

    public interface RowStep {
        ColStep withRow(int rows);
    }

    public interface ColStep {
        EntryGateStep withCol(int cols);
    }

    public interface EntryGateStep {
        ExitGateStep withEntryGate(EntryGate entryGate);
    }

    public interface ExitGateStep {
        SlotPoolStep withExitGate(ExitGate exitGate);
    }

    public interface SlotPoolStep {
        DistributionStep withSlotPool(SlotPool pool);
    }

    public interface DistributionStep {
        GridStep withDistribution(SlotDistributionStrategy distributionStrategy);
    }

    public interface GridStep {
        AllocationStep withGrid();
    }

    public interface AllocationStep {
        BuildStep withAllocation(SlotAllocationStrategy allocationStrategy);
    }

    public interface BuildStep {
        Floor build();
    }

    static class Builder implements floorStep, RowStep, ColStep, EntryGateStep, ExitGateStep, SlotPoolStep, GridStep,
            AllocationStep, DistributionStep, BuildStep {
        private String floorId;
        private int rows;
        private int cols;
        private ParkingSlot[][] grid;
        private EntryGate entryGate;
        private ExitGate exitGate;

        private SlotPool pool;
        private SlotInitializer slotInit;
        private SlotAllocationStrategy allocationStrategy;
        private SlotDistributionStrategy distributionStrategy;

        @Override
        public RowStep withFloor(String floorId) {
            this.floorId = floorId;
            return this;
        }

        @Override
        public ColStep withRow(int rows) {
            this.rows = rows;
            return this;
        }

        @Override
        public EntryGateStep withCol(int cols) {
            this.cols = cols;
            return this;
        }

        @Override
        public ExitGateStep withEntryGate(EntryGate entryGate) {
            this.entryGate = entryGate;
            return this;
        }

        @Override
        public SlotPoolStep withExitGate(ExitGate exitGate) {
            this.exitGate = exitGate;
            return this;
        }

        @Override
        public DistributionStep withSlotPool(SlotPool pool) {
            this.pool = pool;
            return this;
        }

        @Override
        public GridStep withDistribution(SlotDistributionStrategy distributionStrategy) {
            this.distributionStrategy = distributionStrategy;
            this.slotInit = new SlotInitializer(rows, cols, distributionStrategy, pool, entryGate, floorId);
            return this;
        }

        @Override
        public AllocationStep withGrid() {
            this.grid = slotInit.initialize();
            return this;
        }

        @Override
        public BuildStep withAllocation(SlotAllocationStrategy allocationStrategy) {
            this.allocationStrategy = allocationStrategy;
            return this;
        }

        @Override
        public Floor build() {
            return new Floor(this);
        }

    }

    public ParkingSlot getNearestSlot(SlotType type) throws NoSlotAvailableException{
        return allocationStrategy.getNearestSlot(type);
    }

    public void releaseSlot(ParkingSlot slot){
        pool.releaseSlot(slot);
    }

    public int getAvailableCountByType(SlotType type){
        return pool.getAvailableCountByType(type);
    }   
    public EntryGate getEntryGate(){
        return entryGate;
    }
    public String getFloorId(){
        return floorId;
    }
}
