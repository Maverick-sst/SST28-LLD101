package designs.ParkingLot;

import java.util.Objects;

public class ParkingSlot {
    private final String id;
    private final int row;
    private final int col;
    private final SlotType slotType;
    private final String floorId;
    private boolean isOccupied;
    private final double distanceFromGate;

    public ParkingSlot(String id, int row, int col, SlotType slotType, String floorId, double distanceFromGate){
        this.id = id;
        this.row = row;
        this.col = col;
        this.slotType = slotType;
        this.floorId = floorId;
        this.isOccupied = false;
        this.distanceFromGate = distanceFromGate;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)return true;
        if(o == null || getClass() != o.getClass())return false;
        ParkingSlot slot = (ParkingSlot) o;
        
        return row == slot.row &&
               col == slot.col && 
               floorId.equals(slot.floorId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(floorId,row,col);
    }
    public String getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public SlotType getSlotType() {
        return slotType;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }

    public double getDistanceFromGate() {
        return distanceFromGate;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public String getFloorId() {
        return floorId;
    }

    
    

}
