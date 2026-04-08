package designs.DistributedCache;
// currently this is singleton
// used by cache as fallback option
// but instead of creating new threads for each ops
// we can have a db connection pool 
// and threads can avail this from pool to avoid high cost of creating them new each time
// to avoid db overload

import designs.DistributedCache.DbConnectionPool.SimulatedConnection;

public class DbService {
    private static volatile DbService instance;
    private DbConnectionPool pool;

    private DbService(DatabaseAdapter dbAdapter){
        this.pool = DbConnectionPool.INSTANCE.init(dbAdapter, 10);
     };
    
    public static DbService getInstance(){
        if(instance != null)return instance;
        synchronized(DbService.class){
            if(instance != null)return instance;
            instance = new DbService(new MongoAdapter());
            return instance;
        }
    }

    public String executeQuery(String key) throws Exception{
        SimulatedConnection conn= null;
        try {
            conn = pool.getConnection();
            String response =conn.executeQuery(key);
            return response;

        } catch (Exception e) {
            throw new Exception(e);
        }finally{
            if(conn != null){
                pool.releaseConnection(conn);
            }
        }
    }
}
