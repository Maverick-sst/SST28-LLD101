package designs.proxy;

// token bucket algorithm----->

public class RateLimiter {
    private final long capacity;
    private long requests;
    private final long refillRate;
    private long lastRefillTimestamp;

    public RateLimiter(){
        this.capacity = 100;
        this.requests = capacity;
        this.refillRate = 100;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest(){
        refill();
        if(requests > 0){
            requests --;
            return true;
        }
        return false;
    }
    private void refill(){
        long now = System.currentTimeMillis();
        long elapsedTimeInMin = (now - lastRefillTimestamp) / 60000;
        if(elapsedTimeInMin > 0){
            requests = Math.min(capacity, requests + (elapsedTimeInMin * refillRate));
            lastRefillTimestamp = now;
        }
    }
}
