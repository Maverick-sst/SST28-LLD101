package designs.proxy;

public class ApiValidator {
    private final String validKey;
    public ApiValidator(String apiKey){
        this.validKey = apiKey;
    }
    public  boolean isValid(String apiKey){
        return apiKey!=null && validKey.equals(apiKey) ? true : false;
    }
}
