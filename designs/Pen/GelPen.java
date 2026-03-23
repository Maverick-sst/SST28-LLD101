package designs.Pen;

public class GelPen extends Pen{
    public GelPen(String colour, CapStrategy capStrategy){
        super.type = "GEL";
        super.inkColour = colour;
        super.inkPercentage = 100.00;
        super.isReady = false;
        super.capStrategy = capStrategy;
    }

    @Override
    void start() {
        super.isReady = true;
        super.capStrategy.start();
        
    }
    @Override
    void write() throws Exception {
        double updatedInkPercentage = WriteStrategy.write(super.sc, type, isReady, inkPercentage);
        super.inkPercentage = updatedInkPercentage;
    }

    @Override
    void refill(String toChange) {
        System.out.println(super.inkPercentage);

        RefillResponse res = RefillStrategy.refill(super.inkColour, toChange, inkPercentage);
        super.inkColour = res.getColour();
        super.inkPercentage = res.getInkPercentage();
        System.out.println("Refilled Pen with "+ super.inkColour + "has "+ super.inkPercentage + "%");
    }
    @Override
    void close() {
        super.isReady= false;
        super.capStrategy.close();
        System.out.println("Pen Closed for writing");
    }


}
