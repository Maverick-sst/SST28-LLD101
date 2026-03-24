package designs.Pen;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws Exception {
        PenFactory factory = new PenFactory();
        Scanner sc = new Scanner(System.in);

        Pen pen = factory.getPen("gel", "Blue", true);
        pen.start();
        
        String txt = sc.nextLine();
        pen.write(txt);
        
        pen.refill("red");
        pen.close();
    }
}
