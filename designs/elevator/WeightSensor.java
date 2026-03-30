package designs.elevator;

public class WeightSensor {
    // publisher
    private Elevator elevator;
    private float currentWeight;
    private final float maxCapacity;

    public WeightSensor(float maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentWeight = 0;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public void onWeightChange(float weight) {
        this.currentWeight = weight;
        if (weight > maxCapacity) {
            publish(new Event("OVERWEIGHT"));
        }
    }

    public void publish(Event e) {
        if (elevator != null) {
            elevator.handleSensorEvent(e);
        }
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }
}
