package designs.Pen;

public class BallPen extends Pen {
    public BallPen(String colour, CapStrategy capStrategy){
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
        RefillResponse res = RefillStrategy.refill(super.inkColour, toChange, inkPercentage);
        super.inkColour = res.getColour();
        super.inkPercentage = res.getInkPercentage();
    }
    @Override
    void close() {
        super.isReady= false;
        super.capStrategy.close();
        System.out.println("Pen Closed for writing");
    }

}
