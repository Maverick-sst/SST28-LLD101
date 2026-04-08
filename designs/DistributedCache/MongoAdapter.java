package designs.DistributedCache;

public class MongoAdapter implements DatabaseAdapter{

    @Override
    public String getUrl() {
        return MongoDbRepository.INSTANCE.getURL();
    }

    @Override
    public String getUsername() {
        return MongoDbRepository.INSTANCE.getUsername();
    }

    @Override
    public String getPassword() {
        return MongoDbRepository.INSTANCE.getPassword();
    }

    @Override
    public String executeQuery(String key) {
       return MongoDbRepository.INSTANCE.getValue(key);
    }
    
}
