package designs.Pen;

public class UnCapped implements CapStrategy {
    @Override
    public void start() {
        System.out.print("Pen Clicked!\n Ready to write!");
    }
    @Override
    public void close() {
        System.out.print("Pen Unclicked! ");
    }
}
