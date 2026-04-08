package designs.DistributedCache;

public class RequestDTO {
    // keeping fields string for easier implementation + hashing ease
    // its easy for us to serialize a string
    // currently the String interface will handle for null values and we can deal with it
    // we can addd notations saying @Nullable to make the intent clear
    
    private final String key;
    private final String value; // optional --> absent in get request
    private final String operation;

    public RequestDTO(String key, String value, String operation){
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }
    
}
