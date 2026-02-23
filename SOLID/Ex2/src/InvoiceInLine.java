public class InvoiceInLine {
    public final String itemName;
    public final int qty;
    public final double lineTotal;

    public InvoiceInLine(String itemName, int qty, double lineTotal){
        this.itemName=itemName;
        this.qty=qty;
        this.lineTotal=lineTotal;
    }
}
