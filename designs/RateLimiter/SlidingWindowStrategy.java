package designs.RateLimiter;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Sliding Window Log Rate Limiting Strategy
 * 
 * Maintains a log (deque) of request timestamps. On each request:
 *   1. Evict all entries older than windowSizeMs
 *   2. If the log size is below maxRequests, allow and record the request
 *   3. Otherwise, reject the request
 * 
 * Properties:
 *  - More accurate than Fixed Window (no boundary-burst problem)
 *  - Higher memory usage — stores one timestamp per request in the window
 *  - Uses ConcurrentLinkedDeque as the underlying structure
 */
public class SlidingWindowStrategy implements RateLimiterProxy {

    private final int maxRequests;
    private final long windowSizeMs;
    private final ConcurrentLinkedDeque<Long> requestLog;

    /**
     * @param maxRequests   Maximum allowed requests within the sliding window
     * @param windowSizeMs  Window duration in milliseconds
     */
    public SlidingWindowStrategy(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
        this.requestLog = new ConcurrentLinkedDeque<>();
    }

    /** Convenience constructor — 5 requests per 2-second window */
    public SlidingWindowStrategy() {
        this(5, 2000);
    }

    @Override
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        evictExpired(now);

        if (requestLog.size() < maxRequests) {
            requestLog.addLast(now);
            return true;
        }
        return false;
    }

    /**
     * Removes all timestamps from the front of the deque that fall
     * outside the current sliding window.
     */
    private void evictExpired(long now) {
        while (!requestLog.isEmpty() && (now - requestLog.peekFirst()) > windowSizeMs) {
            requestLog.pollFirst();
        }
    }
}
