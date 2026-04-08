package designs.DistributedCache;
import java.util.ArrayList;
import java.util.List;
// making this enum instead
// for inherent singleton behaviour
// critical for connectionPool

public enum DbConnectionPool {
    INSTANCE;

    private DatabaseAdapter dbAdapter;
    private final List<SimulatedConnection> availableConnections;
    private final List<SimulatedConnection> busyConnections;
    private int MAX_POOL_SIZE;

    DbConnectionPool() {        
        this.availableConnections = new ArrayList<>();
        this.busyConnections = new ArrayList<>();
    }
    public DbConnectionPool init(DatabaseAdapter dbAdapter,int poolSize){
        this.dbAdapter = dbAdapter;
        this.MAX_POOL_SIZE = poolSize;
        initializePool();
        return this;
    }
    private void initializePool() {
        for (int i = 0; i <MAX_POOL_SIZE ; i++) {
            availableConnections.add(new SimulatedConnection(dbAdapter));
        }
    }

    public synchronized SimulatedConnection getConnection() {
        while (availableConnections.isEmpty()) {
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return null; }
        }
        SimulatedConnection conn = availableConnections.remove(availableConnections.size() - 1);
        busyConnections.add(conn);
        return conn;
    }

    public synchronized void releaseConnection(SimulatedConnection conn) {
        busyConnections.remove(conn);
        availableConnections.add(conn);
        notifyAll();
    }

    private DatabaseAdapter getDbAdapter() {
        return dbAdapter;
    }
    
    // Inner class simulating a real database connection
    public static class SimulatedConnection {
        private final String url;
        private final String username;
        private final String password;
        private boolean closed = false;

        // Constructor uses DbAdapter to get real connection details
        public SimulatedConnection(DatabaseAdapter adapter) {
            this.url = adapter.getUrl();
            this.username = adapter.getUsername();
            this.password = adapter.getPassword();
            // In a real scenario, DriverManager.getConnection(url, username, password) would be called here.
        }

        // Simulate a simple query using the adapter's executeQuery method
        public String executeQuery(String key) {
            if (closed) {
                throw new IllegalStateException("Connection is closed");
            }
            return getDbAdapter().executeQuery(key);
        }

        // Simulate closing the connection
        public void close() {
            if (!closed) {
                // In a real pool, this would return the connection to the pool
                DbConnectionPool.INSTANCE.releaseConnection(this);
                closed = true;
            }
        }

        // Getter for the adapter to perform operations
        private DatabaseAdapter getDbAdapter() {
            return DbConnectionPool.INSTANCE.getDbAdapter();
        }

        // Utility methods to inspect the simulated connection
        public String getUrl() { return url; }
        public String getUsername() { return username; }
        public boolean isClosed() { return closed; }
    }
}   