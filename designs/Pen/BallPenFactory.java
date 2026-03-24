package designs.Pen;

public class BallPenFactory {
    public static Pen getPen(String type, String colour, boolean hasCap) {
        if(hasCap){
            return new BallPen(colour, new Capped());
        }
        return new BallPen(colour, new UnCapped());
    }
}
