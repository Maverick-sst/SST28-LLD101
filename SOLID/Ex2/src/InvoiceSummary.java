import java.util.List;

public  class InvoiceSummary {
    public final String invId;
    public final List<InvoiceInLine> lines;
    public final Double subtotal;
    public final Double taxPct;
    public final Double tax;
    public final Double discount;
    public final Double total;

    public InvoiceSummary(String invId, List<InvoiceInLine> lines, Double subtotal, Double taxPct, Double tax, Double discount, Double total){
        this.invId=invId;
        this.lines=lines;
        this.subtotal=subtotal;
        this.taxPct=taxPct;
        this.tax=tax;
        this.discount=discount;
        this.total=total;

    }
}
