package designs.elevator;

public class ElevatorRequest {
    private int floorNo;
    private Direction direction;
    private long timeStamp;

    public ElevatorRequest(int floorNo, Direction direction, long timeStamp){
        this.floorNo = floorNo;
        this.direction = direction;
        this.timeStamp = timeStamp;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public Direction getDirection() {
        return direction;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
}
