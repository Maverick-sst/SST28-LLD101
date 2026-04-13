package designs.proxy;

public class GeoLocatorService implements IGeoLocator {
    @Override
    public String locate(String ip,String apikey) {
        return ApiService.locate();
    }
} 