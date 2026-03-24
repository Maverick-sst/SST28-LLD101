package designs.Pen;

public class PenFactory {
    Pen getPen(String type, String colour, boolean hasCap) throws Exception{
        if(type.equalsIgnoreCase("gel")){
            return GelPenFactory.getPen(type, colour, hasCap);
        }else if(type.equalsIgnoreCase("ball")){
            return BallPenFactory.getPen(type, colour, hasCap);
        }else if(type.equalsIgnoreCase("ink")){
            return InkPenFactory.getPen(type, colour, hasCap);
        }
        else{
            throw new Exception("Other Pens aren't allowed Yet!");
        }
        
    }
} 
