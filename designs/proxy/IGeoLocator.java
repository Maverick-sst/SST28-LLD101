package designs.proxy;

public interface IGeoLocator {
    String locate(String ip,String apiKey) throws Exception;
}
