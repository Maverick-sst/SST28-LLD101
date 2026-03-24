package designs.Pen;

public class InkPenFactory {
    public static Pen getPen(String type, String colour, boolean hasCap) {
        if(hasCap){
            return new InkPen(colour, new Capped());
        }
        return new InkPen(colour, new UnCapped());
    }
}
