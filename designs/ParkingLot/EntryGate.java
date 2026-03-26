package designs.ParkingLot;

public class EntryGate {
    private final int row;
    private final int col;
    private final String gateId;

    public EntryGate(int row, int col, String gateId){
        this.row = row;
        this.col = col;
        this.gateId = gateId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getGateId() {
        return gateId;
    }
    
}
