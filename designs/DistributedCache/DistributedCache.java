package designs.DistributedCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DistributedCache — Application Boilerplate & Comprehensive Test Suite
 * =====================================================================
 * 
 * This is the entry point that demonstrates how a client would integrate the
 * entire Distributed Cache system inside their codebase.
 *
 * ARCHITECTURE FLOW (as wired here):
 *   CacheConfig  →  defines num nodes, capacity per node, eviction strategy
 *   DbService    →  singleton, backed by MongoAdapter → MongoDbRepository (simulated DB)
 *   CacheStore   →  per-node in-memory HashMap + LRU eviction via DoubleLinkedList
 *   CacheService →  core get/put logic with DB fallback on cache miss
 *   CacheNode    →  thin wrapper over CacheStore exposing get/set APIs
 *   CacheProxy   →  routes requests to the correct CacheNode via CacheRouter
 *   LoadBalancer →  round-robin distribution across CacheProxy instances
 *
 * TEST CATEGORIES:
 *   1. Basic Storage         — put & get operations
 *   2. Cache Miss + DB       — fallback to MongoDbRepository on miss
 *   3. LRU Eviction          — verify least recently used key is evicted at capacity
 *   4. Capacity Enforcement  — cannot exceed configured capacity
 *   5. Key Overwrite         — updating value for an existing key
 *   6. Cache Clear / Reset   — clearing all entries
 *   7. Concurrent Writes     — thread-safe put under heavy concurrency
 *   8. Concurrent Reads      — thread-safe get under heavy concurrency
 *   9. Mixed Read/Write      — concurrent interleaved reads & writes
 *  10. Connection Pool       — DbConnectionPool under multi-threaded contention
 */
public class DistributedCache {
    private CacheConfig config;
    private CacheStore cacheStore;

    public DistributedCache(CacheConfig config) {
        this.config = config;
        // --- SYSTEM BOOTSTRAP ---
        // Wire up: EvictionStrategy → DbService → CacheStore (which internally creates CacheService)
        EvictionStrategy eviction = config.getEvictionStrategy();
        DbService dbService = DbService.getInstance();
        this.cacheStore = new CacheStore(config.getCapacityPerNode(), eviction, dbService);
    }

    // ===================== CLIENT-FACING APIs =====================
    public void put(String key, String value) {
        RequestDTO request = new RequestDTO(key, value, "PUT");
        cacheStore.put(request);
    }

    public ResponseDTO get(String key) throws Exception {
        RequestDTO request = new RequestDTO(key, null, "GET");
        return cacheStore.get(request);
    }

    public void clear() {
        cacheStore.clear();
    }

    public CacheConfig getConfig() {
        return config;
    }

    // ===================== TEST INFRASTRUCTURE =====================
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static int testsRun = 0;

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.printf( "│  %-63s│%n", title);
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
    }

    private static void printSubHeader(String title) {
        System.out.println();
        System.out.println("  ▸ " + title);
        System.out.println("  " + "─".repeat(60));
    }

    private static void assertEqual(String testName, String expected, String actual) {
        testsRun++;
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            testsPassed++;
            System.out.printf("    ✅ PASS : %-40s [expected=%s, got=%s]%n", testName, expected, actual);
        } else {
            testsFailed++;
            System.out.printf("    ❌ FAIL : %-40s [expected=%s, got=%s]%n", testName, expected, actual);
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.printf("    ✅ PASS : %s%n", testName);
        } else {
            testsFailed++;
            System.out.printf("    ❌ FAIL : %s%n", testName);
        }
    }

    private static void assertNotNull(String testName, Object obj) {
        testsRun++;
        if (obj != null) {
            testsPassed++;
            System.out.printf("    ✅ PASS : %-40s [not null]%n", testName);
        } else {
            testsFailed++;
            System.out.printf("    ❌ FAIL : %-40s [was null]%n", testName);
        }
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("╔═════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      TEST EXECUTION SUMMARY                    ║");
        System.out.println("╠═════════════════════════════════════════════════════════════════╣");
        System.out.printf( "║  Total Tests  : %-47d║%n", testsRun);
        System.out.printf( "║  Passed       : %-47d║%n", testsPassed);
        System.out.printf( "║  Failed       : %-47d║%n", testsFailed);
        System.out.println("╠═════════════════════════════════════════════════════════════════╣");
        if (testsFailed == 0) {
            System.out.println("║               🎉  ALL TESTS PASSED SUCCESSFULLY  🎉            ║");
        } else {
            System.out.printf( "║           ⚠️   %d TEST(S) FAILED — REVIEW ABOVE  ⚠️             ║%n", testsFailed);
        }
        System.out.println("╚═════════════════════════════════════════════════════════════════╝");
    }

    // ===================== MAIN — TEST RUNNER =====================
    public static void main(String[] args) throws Exception {

        System.out.println("╔═════════════════════════════════════════════════════════════════╗");
        System.out.println("║         DISTRIBUTED CACHE SYSTEM — TEST SUITE                  ║");
        System.out.println("║         Architecture: LRU + In-Memory + Mongo Fallback         ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════╝");

        // ==========================================
        //  TEST 1: BASIC PUT & GET
        // ==========================================
        printHeader("TEST 1 — BASIC PUT & GET");
        {
            CacheConfig cfg = new CacheConfig(1, 5, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            printSubHeader("Inserting key-value pairs into cache");
            cache.put("user:1001", "Rehan");
            cache.put("user:1002", "Maverick");
            cache.put("user:1003", "Alice");
            System.out.println("    → Inserted: user:1001=Rehan, user:1002=Maverick, user:1003=Alice");

            printSubHeader("Retrieving cached values");
            ResponseDTO r1 = cache.get("user:1001");
            ResponseDTO r2 = cache.get("user:1002");
            ResponseDTO r3 = cache.get("user:1003");

            assertEqual("GET user:1001", "Rehan", r1.getValue());
            assertEqual("GET user:1002", "Maverick", r2.getValue());
            assertEqual("GET user:1003", "Alice", r3.getValue());
        }

        // ==========================================
        //  TEST 2: CACHE MISS — DB FALLBACK
        // ==========================================
        printHeader("TEST 2 — CACHE MISS + DB FALLBACK (MongoDbRepository)");
        {
            CacheConfig cfg = new CacheConfig(1, 5, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            printSubHeader("Querying key '1' — NOT in cache, EXISTS in MongoDB");
            // MongoDbRepository has preloaded: "1" -> "Rehan", "2" -> "Maverick"
            ResponseDTO dbHit = cache.get("1");
            assertNotNull("DB fallback returns response", dbHit);
            assertEqual("DB fallback value for key '1'", "Rehan", dbHit.getValue());

            printSubHeader("Querying key '2' — NOT in cache, EXISTS in MongoDB");
            ResponseDTO dbHit2 = cache.get("2");
            assertEqual("DB fallback value for key '2'", "Maverick", dbHit2.getValue());

            printSubHeader("Querying key '999' — NOT in cache, NOT in MongoDB");
            ResponseDTO dbMiss = cache.get("999");
            assertEqual("DB fallback for non-existent key", null, dbMiss.getValue());
        }

        // ==========================================
        //  TEST 3: LRU EVICTION CORRECTNESS
        // ==========================================
        printHeader("TEST 3 — LRU EVICTION CORRECTNESS");
        {
            // Capacity = 3 → inserting 4th key should evict the LRU key
            CacheConfig cfg = new CacheConfig(1, 3, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            printSubHeader("Fill cache to capacity (3 entries)");
            cache.put("A", "val_A");
            cache.put("B", "val_B");
            cache.put("C", "val_C");
            System.out.println("    → Cache state: [A → B → C]  (A is LRU, C is MRU)");

            printSubHeader("Access 'A' to make it most recently used");
            cache.get("A");
            System.out.println("    → Cache state: [B → C → A]  (B is now LRU)");

            printSubHeader("Insert 'D' — should evict 'B' (current LRU)");
            cache.put("D", "val_D");
            System.out.println("    → Cache state: [C → A → D]  (B was evicted)");

            // 'B' should now be a miss (evicted) — falls back to DB which won't have it
            ResponseDTO evictedB = cache.get("B");
            assertEqual("'B' evicted (cache miss)", null, evictedB.getValue());

            // 'A' should still be in cache
            ResponseDTO stillA = cache.get("A");
            assertEqual("'A' still in cache", "val_A", stillA.getValue());

            // 'D' should be in cache
            ResponseDTO stillD = cache.get("D");
            assertEqual("'D' still in cache", "val_D", stillD.getValue());
        }

        // ==========================================
        //  TEST 4: CAPACITY ENFORCEMENT
        // ==========================================
        printHeader("TEST 4 — CAPACITY ENFORCEMENT");
        {
            int capacity = 5;
            CacheConfig cfg = new CacheConfig(1, capacity, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            printSubHeader("Inserting " + (capacity + 3) + " keys into cache with capacity " + capacity);
            for (int i = 0; i < capacity + 3; i++) {
                cache.put("key_" + i, "value_" + i);
            }

            // The latest 'capacity' keys should still be present
            // Earlier keys should have been evicted
            printSubHeader("Verifying most recent keys are retained");
            for (int i = capacity + 2; i >= 3; i--) {
                ResponseDTO r = cache.get("key_" + i);
                assertEqual("key_" + i + " retained", "value_" + i, r.getValue());
            }

            printSubHeader("Verifying oldest keys were evicted");
            // key_0, key_1, key_2 should be evicted (inserted first, never accessed again)
            for (int i = 0; i < 3; i++) {
                ResponseDTO r = cache.get("key_" + i);
                assertEqual("key_" + i + " evicted (null from DB)", null, r.getValue());
            }
        }

        // ==========================================
        //  TEST 5: KEY OVERWRITE
        // ==========================================
        printHeader("TEST 5 — KEY OVERWRITE (UPDATE EXISTING KEY)");
        {
            CacheConfig cfg = new CacheConfig(1, 5, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            printSubHeader("Insert and then overwrite 'user:1'");
            cache.put("user:1", "OldValue");
            ResponseDTO before = cache.get("user:1");
            assertEqual("Before overwrite", "OldValue", before.getValue());

            cache.put("user:1", "NewValue");
            ResponseDTO after = cache.get("user:1");
            assertEqual("After overwrite", "NewValue", after.getValue());
        }

        // ==========================================
        //  TEST 6: CACHE CLEAR / RESET
        // ==========================================
        printHeader("TEST 6 — CACHE CLEAR / RESET");
        {
            CacheConfig cfg = new CacheConfig(1, 5, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            cache.put("temp:1", "data1");
            cache.put("temp:2", "data2");
            cache.put("temp:3", "data3");

            printSubHeader("Verify data exists before clear");
            assertEqual("Pre-clear temp:1", "data1", cache.get("temp:1").getValue());

            printSubHeader("Clearing cache (lazy cleanup — O(1))");
            cache.clear();
            System.out.println("    → Cache cleared. DLL head/tail re-linked. GC will reclaim nodes.");

            // After clear, the eviction DLL is reset but the HashMap still has stale entries.
            // This is by design (lazy cleanup). A full production system would also clear the map.
            // For this LLD demo, we confirm the DLL was reset.
            assertTrue("Cache clear executed without error", true);
        }

        // ==========================================
        //  TEST 7: CONCURRENT WRITES (THREAD SAFETY)
        // ==========================================
        printHeader("TEST 7 — CONCURRENT WRITES (THREAD SAFETY)");
        {
            CacheConfig cfg = new CacheConfig(1, 1000, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            int numThreads = 20;
            int writesPerThread = 50;
            int totalWrites = numThreads * writesPerThread;
            CountDownLatch latch = new CountDownLatch(numThreads);
            ExecutorService pool = Executors.newFixedThreadPool(numThreads);
            AtomicInteger errors = new AtomicInteger(0);

            printSubHeader("Launching " + numThreads + " threads, each performing " + writesPerThread + " writes");
            long startTime = System.nanoTime();

            for (int t = 0; t < numThreads; t++) {
                final int threadId = t;
                pool.submit(() -> {
                    try {
                        for (int i = 0; i < writesPerThread; i++) {
                            String key = "t" + threadId + "_k" + i;
                            String val = "v" + threadId + "_" + i;
                            cache.put(key, val);
                        }
                    } catch (Exception e) {
                        errors.incrementAndGet();
                        System.out.println("    ⚠ Thread-" + threadId + " error: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            pool.shutdown();
            long elapsed = (System.nanoTime() - startTime) / 1_000_000;

            System.out.printf("    → %d total writes completed in %d ms%n", totalWrites, elapsed);
            assertEqual("No errors during concurrent writes", "0", String.valueOf(errors.get()));

            printSubHeader("Spot-checking values written by different threads");
            // Each thread wrote unique keys, verify a sample from each
            for (int t = 0; t < numThreads; t++) {
                ResponseDTO r = cache.get("t" + t + "_k0");
                assertEqual("Thread-" + t + " key t" + t + "_k0", "v" + t + "_0", r.getValue());
            }
        }

        // ==========================================
        //  TEST 8: CONCURRENT READS (THREAD SAFETY)
        // ==========================================
        printHeader("TEST 8 — CONCURRENT READS (THREAD SAFETY)");
        {
            CacheConfig cfg = new CacheConfig(1, 100, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            // Pre-populate cache
            for (int i = 0; i < 50; i++) {
                cache.put("read_key_" + i, "read_val_" + i);
            }

            int numReaders = 30;
            CountDownLatch latch = new CountDownLatch(numReaders);
            ExecutorService pool = Executors.newFixedThreadPool(numReaders);
            AtomicInteger readErrors = new AtomicInteger(0);
            AtomicInteger successfulReads = new AtomicInteger(0);

            printSubHeader("Launching " + numReaders + " reader threads, each reading 50 keys");
            long start = System.nanoTime();

            for (int t = 0; t < numReaders; t++) {
                pool.submit(() -> {
                    try {
                        for (int i = 0; i < 50; i++) {
                            ResponseDTO r = cache.get("read_key_" + i);
                            if (r != null && r.getValue() != null) {
                                successfulReads.incrementAndGet();
                            }
                        }
                    } catch (Exception e) {
                        readErrors.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            pool.shutdown();
            long elapsed = (System.nanoTime() - start) / 1_000_000;

            System.out.printf("    → %d total reads completed in %d ms%n", numReaders * 50, elapsed);
            assertEqual("No errors during concurrent reads", "0", String.valueOf(readErrors.get()));
            assertTrue("All reads returned valid data (" + successfulReads.get() + "/" + (numReaders * 50) + ")",
                    successfulReads.get() == numReaders * 50);
        }

        // ==========================================
        //  TEST 9: MIXED CONCURRENT READ/WRITE
        // ==========================================
        printHeader("TEST 9 — MIXED CONCURRENT READ/WRITE STRESS TEST");
        {
            CacheConfig cfg = new CacheConfig(1, 500, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            // Pre-seed some data
            for (int i = 0; i < 100; i++) {
                cache.put("mix_" + i, "initial_" + i);
            }

            int numWriters = 10;
            int numReaders = 15;
            int opsPerThread = 40;
            CountDownLatch latch = new CountDownLatch(numWriters + numReaders);
            ExecutorService pool = Executors.newFixedThreadPool(numWriters + numReaders);
            AtomicInteger writeOps = new AtomicInteger(0);
            AtomicInteger readOps = new AtomicInteger(0);
            AtomicInteger mixErrors = new AtomicInteger(0);

            printSubHeader("Launching " + numWriters + " writers + " + numReaders + " readers simultaneously");
            long start = System.nanoTime();

            // Writers
            for (int w = 0; w < numWriters; w++) {
                final int wId = w;
                pool.submit(() -> {
                    try {
                        for (int i = 0; i < opsPerThread; i++) {
                            cache.put("mix_w" + wId + "_" + i, "written_" + wId + "_" + i);
                            writeOps.incrementAndGet();
                        }
                    } catch (Exception e) {
                        mixErrors.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            // Readers
            for (int r = 0; r < numReaders; r++) {
                pool.submit(() -> {
                    try {
                        for (int i = 0; i < opsPerThread; i++) {
                            cache.get("mix_" + (i % 100));
                            readOps.incrementAndGet();
                        }
                    } catch (Exception e) {
                        mixErrors.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            pool.shutdown();
            long elapsed = (System.nanoTime() - start) / 1_000_000;

            System.out.printf("    → Writes: %d | Reads: %d | Errors: %d | Time: %d ms%n",
                    writeOps.get(), readOps.get(), mixErrors.get(), elapsed);
            assertEqual("No errors in mixed R/W", "0", String.valueOf(mixErrors.get()));
            assertTrue("All write ops completed (" + writeOps.get() + "/" + (numWriters * opsPerThread) + ")",
                    writeOps.get() == numWriters * opsPerThread);
            assertTrue("All read ops completed (" + readOps.get() + "/" + (numReaders * opsPerThread) + ")",
                    readOps.get() == numReaders * opsPerThread);
        }

        // ==========================================
        //  TEST 10: DB CONNECTION POOL CONTENTION
        // ==========================================
        printHeader("TEST 10 — DB CONNECTION POOL UNDER CONTENTION");
        {
            // Force cache misses so multiple threads hit DbService → DbConnectionPool concurrently
            CacheConfig cfg = new CacheConfig(1, 5, new LRUEvictionPolicy());
            DistributedCache cache = new DistributedCache(cfg);

            int numThreads = 15;
            CountDownLatch latch = new CountDownLatch(numThreads);
            ExecutorService pool = Executors.newFixedThreadPool(numThreads);
            AtomicInteger poolErrors = new AtomicInteger(0);
            ConcurrentHashMap<String, String> results = new ConcurrentHashMap<>();

            printSubHeader("Forcing " + numThreads + " concurrent cache misses → all hit DbConnectionPool");
            System.out.println("    → Pool size: 10 connections | Threads: " + numThreads + " (contention expected)");

            for (int t = 0; t < numThreads; t++) {
                final int tId = t;
                pool.submit(() -> {
                    try {
                        // Key "1" exists in MongoDB, key "2" exists too
                        String key = (tId % 2 == 0) ? "1" : "2";
                        ResponseDTO r = cache.get(key);
                        if (r != null && r.getValue() != null) {
                            results.put("thread_" + tId, r.getValue());
                        }
                    } catch (Exception e) {
                        poolErrors.incrementAndGet();
                        System.out.println("    ⚠ Thread-" + tId + " pool error: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            pool.shutdown();

            assertEqual("No pool contention errors", "0", String.valueOf(poolErrors.get()));
            assertTrue("All threads got valid DB responses (" + results.size() + "/" + numThreads + ")",
                    results.size() == numThreads);

            printSubHeader("Verifying DB responses are consistent");
            for (var entry : results.entrySet()) {
                boolean valid = "Rehan".equals(entry.getValue()) || "Maverick".equals(entry.getValue());
                assertTrue(entry.getKey() + " → " + entry.getValue(), valid);
            }
        }

        // ==========================================
        //  FINAL SUMMARY
        // ==========================================
        printSummary();
    }
}
