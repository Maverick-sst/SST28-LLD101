package designs.RateLimiter;

public class Client {
    public static void main(String[] args) {
        DataService service = new DataService(new RateLimitingStrategy());
        System.out.println(service.getData());
    }
}
