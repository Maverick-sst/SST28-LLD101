package designs.Pen;

import java.util.Scanner;

public class WriteStrategy {

    static double write(Scanner sc, String type, boolean isReady, double inkPercentage) throws Exception {
        // we need to check if pen can write
        // we need to check the ink % == 0.00 cannot write ----> refill
        // else write as long as the ink lasts which in our case is decided to last for
        // nearly 10,000 chars----> 100% refill
        // so writing 100 chars ----> -1% of ink from our total
        if (isReady) {
            if (inkPercentage == 0.00)
                throw new Exception("Cannot Write , Please Refill Ink First !"); // refill
            else {
                System.out.println(type + " starts writing!");
                String txt = sc.nextLine();
                // String pureCharacters = txt.replaceAll("\\s","");
                int idx = 0;
                while (inkPercentage != 0.00 && idx != txt.length()) {
                    if (txt.charAt(idx) != ' ') {
                        System.out.print(txt.charAt(idx));
                        idx++;
                        inkPercentage -= 0.01;
                    }else{
                        System.out.print(txt.charAt(idx));
                        idx++;
                    }
                    
                }
            }
            return inkPercentage;
        } else {
            throw new Exception("Cannot write! ");
        }
    }
}
