package designs.Pen;

public class PenFactory {
    Pen getPen(String type, String colour, boolean hasCap) throws Exception{
        if(type.toLowerCase() == "gel"){
            return GelPenFactory.getPen(type, colour, hasCap);
        }else if(type.toLowerCase() == "ball"){
            return BallPenFactory.getPen(type, colour, hasCap);
        }else if(type.toLowerCase() == "ink"){
            return InkPenFactory.getPen(type, colour, hasCap);
        }
        else{
            throw new Exception("Other Pens aren't allowed Yet!");
        }
        
    }
} 
