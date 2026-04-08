package designs.DistributedCache;

import java.util.HashMap;
import java.util.Map;

public enum MongoDbRepository {
    INSTANCE;
    
    private final Map<String,String> database = new HashMap<>();
    private final String URL = "https://mongodb:21704/dummy";
    private final String username = "Admin_1";
    private final String password = "Admin_1@!@#";

    MongoDbRepository(){
        // dummy data loaded during initialization
        database.put("1", "Rehan");
        database.put("2", "Maverick");
    }
    public String getValue(String key){
        return database.get(key);
    }
    public void setValue(String key,String value){
        database.put(key, value);
    }
    public String getURL() {
        return URL;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

}
