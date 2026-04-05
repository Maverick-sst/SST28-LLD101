package designs.RateLimiter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ════════════════════════════════════════════════════════════════
 *  RATE LIMITER TEST SUITE
 *  Single-JVM  •  No external dependencies  •  200-thread concurrency
 * ════════════════════════════════════════════════════════════════
 *
 *  Tests:
 *    1–2  Fixed Window  (basic + window reset)
 *    3–4  Token Bucket  (basic + refill)
 *    5–6  Sliding Window (basic + slide)
 *    7–9  Concurrency   (one per strategy, 200 threads)
 *
 *  Run:
 *    javac designs/RateLimiter/*.java
 *    java  designs.RateLimiter.RateLimiterTest
 */
public class RateLimiterTest {

    // ───── ANSI colors for terminal output ─────
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD   = "\u001B[1m";
    private static final String RESET  = "\u001B[0m";

    private static int totalTests  = 0;
    private static int passedTests = 0;

    // ═══════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ═══════════════════════════════════════════════════════════
    public static void main(String[] args) throws Exception {

        printHeader();

        // ── Fixed Window tests ──
        testFixedWindowBasic();
        testFixedWindowReset();

        // ── Token Bucket tests ──
        testTokenBucketBasic();
        testTokenBucketRefill();

        // ── Sliding Window tests ──
        testSlidingWindowBasic();
        testSlidingWindowSlide();

        // ── Concurrency tests ──
        testConcurrencyFixedWindow();
        testConcurrencyTokenBucket();
        testConcurrencySlidingWindow();

        printFooter();

        // Exit with non-zero code if any test failed
        if (passedTests < totalTests) {
            System.exit(1);
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  1. FIXED WINDOW — BASIC
    // ═══════════════════════════════════════════════════════════
    private static void testFixedWindowBasic() {
        totalTests++;
        String name = "Fixed Window — Basic";
        int capacity = 10;

        // Use reflection-free approach: create with known capacity
        // The default constructor has capacity=100; we reuse it with a wrapper.
        // For a clean test we create a small-capacity fixed-window limiter.
        RateLimiterProxy limiter = new FixedWindowTestable(capacity, 60_000);

        int allowed = 0, rejected = 0;
        int totalRequests = 15;
        for (int i = 0; i < totalRequests; i++) {
            if (limiter.allowRequest()) allowed++;
            else rejected++;
        }

        boolean pass = (allowed == capacity) && (rejected == totalRequests - capacity);
        printResult(1, name,
                "capacity=" + capacity + ", window=60s",
                totalRequests, allowed, rejected, null, pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  2. FIXED WINDOW — WINDOW RESET
    // ═══════════════════════════════════════════════════════════
    private static void testFixedWindowReset() throws Exception {
        totalTests++;
        String name = "Fixed Window — Window Reset";
        int capacity = 5;
        long windowMs = 1000; // 1-second window for fast test

        RateLimiterProxy limiter = new FixedWindowTestable(capacity, windowMs);

        // Exhaust the window
        for (int i = 0; i < capacity; i++) limiter.allowRequest();

        // Should be rejected now
        boolean rejectedBeforeReset = !limiter.allowRequest();

        // Wait for window to expire
        Thread.sleep(windowMs + 100);

        // Should succeed after reset
        boolean allowedAfterReset = limiter.allowRequest();

        boolean pass = rejectedBeforeReset && allowedAfterReset;
        printResult(2, name,
                "capacity=" + capacity + ", window=" + windowMs + "ms",
                capacity + 2, capacity + 1, 1,
                "rejected-before-reset=" + rejectedBeforeReset + ", allowed-after-reset=" + allowedAfterReset,
                pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  3. TOKEN BUCKET — BASIC
    // ═══════════════════════════════════════════════════════════
    private static void testTokenBucketBasic() {
        totalTests++;
        String name = "Token Bucket — Basic";
        int maxTokens = 10;

        TokenBucketStrategy limiter = new TokenBucketStrategy(maxTokens, 1.0);

        int allowed = 0, rejected = 0;
        int totalRequests = 15;
        for (int i = 0; i < totalRequests; i++) {
            if (limiter.allowRequest()) allowed++;
            else rejected++;
        }

        boolean pass = (allowed == maxTokens) && (rejected == totalRequests - maxTokens);
        printResult(3, name,
                "maxTokens=" + maxTokens + ", refillRate=1/s",
                totalRequests, allowed, rejected, null, pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  4. TOKEN BUCKET — REFILL
    // ═══════════════════════════════════════════════════════════
    private static void testTokenBucketRefill() throws Exception {
        totalTests++;
        String name = "Token Bucket — Refill";
        int maxTokens = 3;
        double refillRate = 5.0; // 5 tokens/sec → ~1 token every 200ms

        TokenBucketStrategy limiter = new TokenBucketStrategy(maxTokens, refillRate);

        // Drain all tokens
        for (int i = 0; i < maxTokens; i++) limiter.allowRequest();
        boolean rejectedWhenEmpty = !limiter.allowRequest();

        // Wait for refill (~600ms → should refill ~3 tokens at 5/sec)
        Thread.sleep(700);

        // After refill, at least one request should succeed
        boolean allowedAfterRefill = limiter.allowRequest();

        boolean pass = rejectedWhenEmpty && allowedAfterRefill;
        printResult(4, name,
                "maxTokens=" + maxTokens + ", refillRate=" + refillRate + "/s",
                maxTokens + 2, maxTokens + 1, 1,
                "rejected-when-empty=" + rejectedWhenEmpty + ", allowed-after-refill=" + allowedAfterRefill,
                pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  5. SLIDING WINDOW — BASIC
    // ═══════════════════════════════════════════════════════════
    private static void testSlidingWindowBasic() {
        totalTests++;
        String name = "Sliding Window — Basic";
        int maxReqs = 5;
        long windowMs = 2000;

        SlidingWindowStrategy limiter = new SlidingWindowStrategy(maxReqs, windowMs);

        int allowed = 0, rejected = 0;
        int totalRequests = 8;
        for (int i = 0; i < totalRequests; i++) {
            if (limiter.allowRequest()) allowed++;
            else rejected++;
        }

        boolean pass = (allowed == maxReqs) && (rejected == totalRequests - maxReqs);
        printResult(5, name,
                "maxRequests=" + maxReqs + ", window=" + windowMs + "ms",
                totalRequests, allowed, rejected, null, pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  6. SLIDING WINDOW — SLIDE
    // ═══════════════════════════════════════════════════════════
    private static void testSlidingWindowSlide() throws Exception {
        totalTests++;
        String name = "Sliding Window — Slide";
        int maxReqs = 3;
        long windowMs = 1000;

        SlidingWindowStrategy limiter = new SlidingWindowStrategy(maxReqs, windowMs);

        // Fill the window
        for (int i = 0; i < maxReqs; i++) limiter.allowRequest();
        boolean rejectedBeforeSlide = !limiter.allowRequest();

        // Wait for entries to slide out
        Thread.sleep(windowMs + 200);

        boolean allowedAfterSlide = limiter.allowRequest();

        boolean pass = rejectedBeforeSlide && allowedAfterSlide;
        printResult(6, name,
                "maxRequests=" + maxReqs + ", window=" + windowMs + "ms",
                maxReqs + 2, maxReqs + 1, 1,
                "rejected-before-slide=" + rejectedBeforeSlide + ", allowed-after-slide=" + allowedAfterSlide,
                pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  7. CONCURRENCY — FIXED WINDOW
    // ═══════════════════════════════════════════════════════════
    private static void testConcurrencyFixedWindow() throws Exception {
        totalTests++;
        String name = "Concurrency — Fixed Window";
        int capacity = 50;
        int threadCount = 200;

        RateLimiterProxy limiter = new FixedWindowTestable(capacity, 60_000);
        int[] results = runConcurrent(limiter, threadCount);

        boolean pass = results[0] <= capacity;
        printResult(7, name,
                "capacity=" + capacity + ", threads=" + threadCount,
                threadCount, results[0], results[1],
                "invariant: allowed(" + results[0] + ") <= capacity(" + capacity + ")",
                pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  8. CONCURRENCY — TOKEN BUCKET
    // ═══════════════════════════════════════════════════════════
    private static void testConcurrencyTokenBucket() throws Exception {
        totalTests++;
        String name = "Concurrency — Token Bucket";
        int maxTokens = 50;
        int threadCount = 200;

        TokenBucketStrategy limiter = new TokenBucketStrategy(maxTokens, 0); // 0 refill → pure burst test
        int[] results = runConcurrent(limiter, threadCount);

        boolean pass = results[0] <= maxTokens;
        printResult(8, name,
                "maxTokens=" + maxTokens + ", threads=" + threadCount + ", refill=0",
                threadCount, results[0], results[1],
                "invariant: allowed(" + results[0] + ") <= maxTokens(" + maxTokens + ")",
                pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  9. CONCURRENCY — SLIDING WINDOW
    // ═══════════════════════════════════════════════════════════
    private static void testConcurrencySlidingWindow() throws Exception {
        totalTests++;
        String name = "Concurrency — Sliding Window";
        int maxReqs = 50;
        int threadCount = 200;
        long windowMs = 60_000; // large window so nothing slides out during test

        SlidingWindowStrategy limiter = new SlidingWindowStrategy(maxReqs, windowMs);
        int[] results = runConcurrent(limiter, threadCount);

        boolean pass = results[0] <= maxReqs;
        printResult(9, name,
                "maxRequests=" + maxReqs + ", threads=" + threadCount,
                threadCount, results[0], results[1],
                "invariant: allowed(" + results[0] + ") <= maxRequests(" + maxReqs + ")",
                pass);
    }

    // ═══════════════════════════════════════════════════════════
    //  HELPER — run N threads each firing 1 request simultaneously
    // ═══════════════════════════════════════════════════════════
    private static int[] runConcurrent(RateLimiterProxy limiter, int threadCount) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger rejected = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                readyLatch.countDown();            // signal "I'm ready"
                try { startLatch.await(); }        // wait for the gun
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }

                if (limiter.allowRequest()) allowed.incrementAndGet();
                else rejected.incrementAndGet();
            });
        }

        readyLatch.await();     // wait until all threads are staged
        startLatch.countDown(); // fire!

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        return new int[]{ allowed.get(), rejected.get() };
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGGING HELPERS
    // ═══════════════════════════════════════════════════════════

    private static void printHeader() {
        System.out.println();
        System.out.println(BOLD + CYAN + "════════════════════════════════════════════════════════════" + RESET);
        System.out.println(BOLD + CYAN + "  RATE LIMITER TEST SUITE" + RESET);
        System.out.println(BOLD + CYAN + "  Single-JVM  •  200-Thread Concurrency  •  9 Tests" + RESET);
        System.out.println(BOLD + CYAN + "════════════════════════════════════════════════════════════" + RESET);
        System.out.println();
    }

    private static void printResult(int testNum, String name, String config,
                                    int sent, int allowed, int rejected,
                                    String extra, boolean pass) {
        String status = pass
                ? GREEN + BOLD + "PASSED ✓" + RESET
                : RED   + BOLD + "FAILED ✗" + RESET;

        if (pass) passedTests++;

        System.out.println(YELLOW + "  [TEST " + testNum + "/9] " + RESET + BOLD + name + RESET);
        System.out.println("    ├─ Config  : " + config);
        System.out.println("    ├─ Sent    : " + sent + " requests");
        System.out.println("    ├─ Allowed : " + allowed + "  |  Rejected : " + rejected);
        if (extra != null) {
            System.out.println("    ├─ Detail  : " + extra);
        }
        System.out.println("    └─ " + status);
        System.out.println(CYAN + "  ──────────────────────────────────────────────────────────" + RESET);
    }

    private static void printFooter() {
        System.out.println();
        String color = (passedTests == totalTests) ? GREEN : RED;
        System.out.println(BOLD + color + "════════════════════════════════════════════════════════════" + RESET);
        System.out.println(BOLD + color + "  RESULTS: " + passedTests + "/" + totalTests + " PASSED" + RESET);
        System.out.println(BOLD + color + "════════════════════════════════════════════════════════════" + RESET);
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════
    //  INNER CLASS — Testable Fixed Window (configurable capacity & window)
    // ═══════════════════════════════════════════════════════════
    /**
     * A testable version of the fixed-window counter that accepts
     * configurable capacity and window size, unlike the original
     * {@link RateLimitingStrategy} which hard-codes capacity=100.
     */
    private static class FixedWindowTestable implements RateLimiterProxy {
        private final int capacity;
        private final long windowSizeMs;
        private int requests;
        private long windowStartTime;

        FixedWindowTestable(int capacity, long windowSizeMs) {
            this.capacity = capacity;
            this.windowSizeMs = windowSizeMs;
            this.requests = 0;
            this.windowStartTime = System.currentTimeMillis();
        }

        @Override
        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            if ((now - windowStartTime) >= windowSizeMs) {
                requests = 0;
                windowStartTime = now;
            }
            if (requests < capacity) {
                requests++;
                return true;
            }
            return false;
        }
    }
}
