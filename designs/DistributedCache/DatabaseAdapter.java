package designs.DistributedCache;

public interface DatabaseAdapter {
    String getUrl();
    String getUsername();
    String getPassword();
    String executeQuery(String key);  
} 