package designs.elevator;

public class Floor {
    private final int floorId;
    private OuterPanel outerPanel;

    public Floor(int floorId) {
        this.floorId = floorId;
        this.outerPanel = new OuterPanel(this);
    }

    public int getFloorId() {
        return this.floorId;
    }

    public OuterPanel getOuterPanel() {
        return outerPanel;
    }
}
