package designs.Pen;

public class InkPenFactory {
    public static Pen getPen(String type, String colour, boolean hasCap) {
        if(hasCap){
            return new GelPen(colour, new Capped());
        }
        return new GelPen(colour, new UnCapped());
    }
}
