package designs.Pen;

public class RefillStrategy {
    static RefillResponse refill(String before, String after,double inkPercentage){
        if(before.equals(after)){
            if(inkPercentage < 100.00){
                // refill it with same ink 
                return new RefillResponse(after,100.00);
            }
            return new RefillResponse(before,inkPercentage);
        }else{
            return new RefillResponse(after,100.00);
        }
    }
}
