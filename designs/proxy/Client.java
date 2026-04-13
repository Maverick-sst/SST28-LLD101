package designs.proxy;

public class Client {
    public static void main(String[] args) {
        String ip = "0.0.0.0."; // hardcoded but could be removed
        String apiKey = "my-secret-key";
        IGeoLocator application = Application.create("my-secret-key");
        for (int i = 0; i < 100; i++) {
            try {
                // String response = geoLocator.locate(ip, apiKey);
                // String ip = "0.0.0.0" + i;
                String response = application.locate(ip, apiKey);

                System.out.println(i + ":" + response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
