package designs.Pen;

public class Test {
    public static void main(String[] args) throws Exception {
        PenFactory factory = new PenFactory();
        Pen pen = factory.getPen("gel", "Blue", true);
        pen.start();
        pen.write();
        pen.refill("red");
        pen.close();
    }
}
