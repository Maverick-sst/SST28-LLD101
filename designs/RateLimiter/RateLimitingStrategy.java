package designs.RateLimiter;
// fixed window counting

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitingStrategy implements RateLimiterProxy {
    private final long capacity;
    // private long requests;
    private AtomicInteger requests;
    private long windowStartTime;
    private long windowSize;

    public RateLimitingStrategy() {
        this.capacity = 100; // max 100 req
        this.requests = new AtomicInteger(0);
        this.windowStartTime = System.currentTimeMillis();
        this.windowSize = 60;
    }

    @Override
    public boolean allowRequest() {
        long now = System.currentTimeMillis();
        long elapsedTimeInMin = (now - windowStartTime) / (windowSize * 1000);
        if (elapsedTimeInMin >= windowSize) {
            requests.set(0);

            windowStartTime = now;
        }
        if (requests.get() < capacity) {
            requests.addAndGet(1);
            return true;
        } else {
            return false;
        }
    }
}
