package designs.RateLimiter;

/**
 * Token Bucket Rate Limiting Strategy
 * 
 * Maintains a bucket of tokens that refills at a constant rate.
 * Each request consumes one token. If no tokens are available, the request is rejected.
 * 
 * Properties:
 *  - Allows controlled bursts up to maxTokens
 *  - Smooth refill at a constant rate (tokensPerSecond)
 *  - Lazy refill — tokens are recalculated on each request, not via a background thread
 */
public class TokenBucketStrategy implements RateLimiterProxy {

    private final int maxTokens;
    private final double tokensPerSecond;
    private int availableTokens;
    private long lastRefillTimestamp;

    /**
     * @param maxTokens       Maximum tokens the bucket can hold (burst capacity)
     * @param tokensPerSecond  Rate at which tokens are added back to the bucket
     */
    public TokenBucketStrategy(int maxTokens, double tokensPerSecond) {
        this.maxTokens = maxTokens;
        this.tokensPerSecond = tokensPerSecond;
        this.availableTokens = maxTokens;
        this.lastRefillTimestamp = System.nanoTime();
    }

    /** Convenience constructor — 10 tokens, 1 token/sec */
    public TokenBucketStrategy() {
        this(10, 1.0);
    }

    @Override
    public synchronized boolean allowRequest() {
        refill();

        if (availableTokens > 0) {
            availableTokens--;
            return true;
        }
        return false;
    }

    /**
     * Lazily refills tokens based on elapsed time since the last refill.
     * Caps at maxTokens so the bucket never overflows.
     */
    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000.0;
        int tokensToAdd = (int) (elapsedSeconds * tokensPerSecond);

        if (tokensToAdd > 0) {
            availableTokens = Math.min(maxTokens, availableTokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
}
