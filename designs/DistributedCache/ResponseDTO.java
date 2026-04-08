package designs.DistributedCache;

public class ResponseDTO {
    private String key;
    private String value;

    public ResponseDTO(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
}
