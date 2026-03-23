package designs.Pen;

public class RefillResponse {
    private String colour;
    private double inkPercentage;
    
    public RefillResponse(String colour, double inkPercentage){
        this.colour = colour;
        this.inkPercentage = inkPercentage;
    }
    public String getColour() {
        return colour;
    }
    public double getInkPercentage() {
        return inkPercentage;
    }

    
}
