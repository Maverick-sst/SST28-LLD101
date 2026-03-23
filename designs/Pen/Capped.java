package designs.Pen;

public class Capped implements CapStrategy {
    @Override
    public void start() {
        System.out.print("Cap opened!\n Ready to write!");
    }
    @Override
    public void close() {
        System.out.print("Cap closed!");
    }
}
