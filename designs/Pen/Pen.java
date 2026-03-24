package designs.Pen;

public abstract class Pen {
    private String type;
    private String inkColour;
    private double inkPercentage;
    private boolean isReady;
    private CapStrategy capStrategy;

    protected Pen(String type, String colour, CapStrategy capStrategy) {
        this.type = type;
        this.inkColour = colour;
        this.capStrategy = capStrategy;
        this.inkPercentage = 100.00;
        this.isReady = false;
    }

    void start() {
        this.isReady = true;
        this.capStrategy.start();

    }

    void write(String txt) throws Exception {
        double updatedInkPercentage = WriteStrategy.write(txt, isReady, inkPercentage);
        this.inkPercentage = updatedInkPercentage;
    }

    void refill(String toChange) {
        System.out.println(this.inkPercentage);

        RefillResponse res = RefillStrategy.refill(this.inkColour, toChange, inkPercentage);
        this.inkColour = res.getColour();
        this.inkPercentage = res.getInkPercentage();
        System.out.println("Refilled Pen with " + this.inkColour + "has " + this.inkPercentage + "%");
    }

    void close() {
        this.isReady = false;
        this.capStrategy.close();
        System.out.println("Pen Closed for writing");
    }

}
