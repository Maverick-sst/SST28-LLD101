package designs.RateLimiter;

public class DataService {
    private RateLimiterProxy rl;
    public DataService(RateLimiterProxy rl){
        this.rl = rl;
    }
    public String getData(){
        if(rl.allowRequest()){
            return "This is a dummy data!";
        }
        return "Too many requests, Please Try Again after some time!";
    }
}
