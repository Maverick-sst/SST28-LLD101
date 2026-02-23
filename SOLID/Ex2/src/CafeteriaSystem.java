import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private FileRepo db;
    public CafeteriaSystem(FileRepo db){
        this.db=db;
    }
    private int invoiceSeq = 1000;

    public void addToMenu(MenuItem i) { menu.put(i.id, i); }

    // Intentionally SRP-violating: menu mgmt + tax + discount + format + persistence.
    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);
        
        InvoiceSummary summary = InvoiceCalculator.calculate(invId,customerType, lines, menu);
   
        String printable = InvoiceFormatter.format(summary);
        System.out.print(printable);
        db.save(invId, printable);
        System.out.println("Saved invoice: " + invId + " (lines=" + db.countLines(invId) + ")");
    }
}
