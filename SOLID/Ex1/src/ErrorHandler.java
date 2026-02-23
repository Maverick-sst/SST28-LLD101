import java.util.List;

public class ErrorHandler {
    public static void handleError(List<String> errors){
        System.out.println("ERROR: cannot register");
        for (String e : errors) System.out.println("- " + e);
    }
}
