package designs.proxy;

public class GeoLocatorProxy implements IGeoLocator {
    private final ICache cache;
    private final ApiValidator validator;
    private final IGeoLocator geoLocatorService;
    private final RateLimiter rateLimiter ;
    // private final Application application;
    public GeoLocatorProxy(ApiValidator validator,ICache cache, IGeoLocator service, RateLimiter rateLimiter){
        this.validator = validator;
        this.cache = cache;
        this.geoLocatorService = service;
        this.rateLimiter = rateLimiter;
    }
    boolean authenticate(String apiKey){
        return validator.isValid(apiKey);
    }

    @Override
    public String locate(String ip,String apiKey) throws Exception {
        if(!authenticate(apiKey)){
            System.out.println("Not a valid ApiKey");
            throw new Exception("Api key not valid");
        }
        if(cache.contains(ip))return "Reading from cache: " +cache.get(ip);
        
        if(!rateLimiter.allowRequest()){
            throw new RuntimeException("Rate limit exceeded! (only 100 req/min are allowed)");
        }
        else{
            // retry loop --max 3 times
            // Exception ex= null;
            for(int i=0;i<4;i++){
                try {
                    String response = geoLocatorService.locate(ip, apiKey);
                    cache.put(ip, response);
                    return response;
                } catch (Exception e) {
                    // ex = e;
                    if(e.getMessage().contains("timeout") || e instanceof java.net.SocketTimeoutException){
                        continue;
                    }
                    break;
                }
            }
            throw new Exception("Request failed after 3 retries:");
        }
    }

}
