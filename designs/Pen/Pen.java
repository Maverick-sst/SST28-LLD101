package designs.Pen;

import java.util.Scanner;

public abstract class  Pen {
    String type;
    String inkColour;
    double inkPercentage;
    boolean isReady;
    Scanner sc = new Scanner(System.in);

    CapStrategy capStrategy;
    void start(){

    }
    void write() throws Exception{

    }
    void close(){

    }
    void refill(String colour){

    }

}
