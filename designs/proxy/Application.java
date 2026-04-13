package designs.proxy;
// central huub for all object creation
public class Application {
    public static IGeoLocator create(String apiKey){
        ICache cache = new CacheService();
        RateLimiter limiter = new RateLimiter();
        ApiValidator validator = new ApiValidator(apiKey);
        IGeoLocator service = new GeoLocatorService();

        return new GeoLocatorProxy(validator,cache,service,limiter);
    }
    

}
