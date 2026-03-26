package designs.ParkingLot;

public class Vehicle {
    private final String brand;
    private final String model;
    private final String numberPlate;
    private final String color;

    public Vehicle(String brand, String model, String numberPlate, String color){
        this.brand = brand;
        this.model = model;
        this.numberPlate = numberPlate;
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public String getColor() {
        return color;
    }
    

}
