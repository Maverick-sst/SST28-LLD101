package designs.Pen;

public class WriteStrategy {

    static double write(String text, boolean isReady, double inkPercentage) throws Exception {
        // we need to check if pen can write
        // we need to check the ink % == 0.00 cannot write ----> refill
        // else write as long as the ink lasts which in our case is decided to last for
        // nearly 10,000 chars----> 100% refill
        // so writing 100 chars ----> -1% of ink from our total

        if (!isReady)
            throw new Exception("Cannot write!");
        if (inkPercentage == 0.0)
            throw new Exception("Please refill first!");
        int charsToWrite = (int) Math.min(text.replaceAll("\\s", "").length(), inkPercentage / 0.01);
        return inkPercentage - (charsToWrite * 0.01);
    }
}
